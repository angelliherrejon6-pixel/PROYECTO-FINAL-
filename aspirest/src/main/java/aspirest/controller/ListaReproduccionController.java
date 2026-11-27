package aspirest.controller;

import aspirest.models.ListaReproduccion;
import aspirest.models.ListaReproduccionMedio;  
import aspirest.models.Medio;                   
import aspirest.repo.ListaReproduccionRepository;
import aspirest.repo.MedioRepository;            
import aspirest.repo.UsuarioRepository; 

import io.swagger.v3.oas.annotations.Operation;  
import io.swagger.v3.oas.annotations.tags.Tag;  
import org.springframework.http.ContentDisposition;  
import org.springframework.http.HttpHeaders;         
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.nio.charset.StandardCharsets;  
import java.time.LocalDate;                
import java.util.ArrayList;               
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/listas")
@Tag(name = "Listas de reproducción", description = "CRUD de listas del usuario")
public class ListaReproduccionController {

    private final ListaReproduccionRepository listaReproduccionRepository;
    private final MedioRepository medioRepository;
    private final UsuarioRepository usuarioRepository;

    public ListaReproduccionController(ListaReproduccionRepository listaReproduccionRepository,
                                       MedioRepository medioRepository,
                                       UsuarioRepository usuarioRepository) {
        this.listaReproduccionRepository = listaReproduccionRepository;
        this.medioRepository = medioRepository;
        this.usuarioRepository = usuarioRepository;
    }

     private Long uid(Jwt jwt) {
        return ((Number) jwt.getClaim("uid")).longValue();
    }

    private ListaReproduccion propia(Long id, Jwt jwt) {
        ListaReproduccion lista = listaReproduccionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lista no encontrada"));
        if (!lista.getUsuario().getId().equals(uid(jwt))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Esa lista no es tuya");
        }
        return lista;
    }
    

    @GetMapping
    public List<ListaReproduccion> listar() {
        return listaReproduccionRepository.findAll();
    }

    @Operation(summary = "Consultar mis listas", description = "Devuelve solo las listas del usuario del token.")
    @GetMapping("/mias")
    public List<ListaReproduccion> mias(@AuthenticationPrincipal Jwt jwt) {
        return listaReproduccionRepository.findByUsuarioId(uid(jwt));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListaReproduccion> obtener(@PathVariable Long id) {
        return listaReproduccionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
   @Operation(summary = "Descargar una de mis listas", description = "Devuelve un archivo CSV con los medios de la lista.")
    @GetMapping("/{id}/descargar")
    public ResponseEntity<byte[]> descargar(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        ListaReproduccion lista = propia(id, jwt);
        StringBuilder csv = new StringBuilder("posicion,nombre,artista,album,genero,duracionMinutos,anioLanzamiento\n");
        lista.getMedios().stream()
                .sorted(Comparator.comparing(ListaReproduccionMedio::getPosicion,
                        Comparator.nullsLast(Comparator.<Integer>naturalOrder())))
                .forEach(e -> {
                    Medio m = e.getMedio();
                    csv.append(e.getPosicion()).append(',')
                       .append(celda(m.getNombre())).append(',')
                       .append(celda(m.getArtista())).append(',')
                       .append(celda(m.getAlbum())).append(',')
                       .append(celda(m.getGenero())).append(',')
                       .append(m.getDuracionMinutos() == null ? "" : m.getDuracionMinutos()).append(',')
                       .append(m.getAñoLanzamiento() == null ? "" : m.getAñoLanzamiento()).append('\n');
                });
        String archivo = (lista.getNombre() == null ? "lista" : lista.getNombre().replaceAll("[^\\w\\-]", "_")) + ".csv";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(archivo, StandardCharsets.UTF_8).build().toString())
                .contentType(MediaType.parseMediaType("text/csv;charset=UTF-8"))
                .body(csv.toString().getBytes(StandardCharsets.UTF_8));
    }


    private String celda(String valor) {
        if (valor == null) return "";
        return "\"" + valor.replace("\"", "\"\"") + "\"";
    }

    @Operation(summary = "Crear una lista", description = "La lista se asigna al usuario del token.")
    @PostMapping
    public ResponseEntity<ListaReproduccion> crear(@AuthenticationPrincipal Jwt jwt,
                                                   @RequestBody ListaReproduccion lista) {
        lista.setId(null);
        lista.setUsuario(usuarioRepository.getReferenceById(uid(jwt)));   // CAMBIO
        lista.setMedios(new ArrayList<>());   // CAMBIO: los medios se agregan con /{id}/medios/{medioId}
        return ResponseEntity.status(HttpStatus.CREATED).body(listaReproduccionRepository.save(lista));
    }


    @Operation(summary = "Actualizar una lista", description = "Solo el propietario. Modifica nombre, duración y URL.")
    @PutMapping("/{id}")
    public ResponseEntity<ListaReproduccion> actualizar(@AuthenticationPrincipal Jwt jwt,
                                                        @PathVariable Long id,
                                                        @RequestBody ListaReproduccion datos) {
        ListaReproduccion lista = propia(id, jwt);   // CAMBIO
        lista.setNombre(datos.getNombre());
        lista.setDuracionMinutos(datos.getDuracionMinutos());
        lista.setURL(datos.getURL());
        return ResponseEntity.ok(listaReproduccionRepository.save(lista));
    }

    @Operation(summary = "Eliminar una lista", description = "Solo el propietario.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        ListaReproduccion lista = propia(id, jwt);   // CAMBIO
        listaReproduccionRepository.delete(lista);
        return ResponseEntity.noContent().build();
    }


 main
@Operation(summary = "Agregar un medio a mi lista", description = "Se agrega al final. No permite repetir el mismo medio.")
    @PostMapping("/{id}/medios/{medioId}")
    public ResponseEntity<ListaReproduccion> agregarMedio(@AuthenticationPrincipal Jwt jwt,
                                                          @PathVariable Long id, @PathVariable Long medioId) {

    @Operation(summary = "Agregar un medio a mi lista", description = "Se agrega al final. No permite repetir el mismo medio.")
    @PostMapping("/{id}/medios/{medioId}")
    public ListaReproduccion agregarMedio(@AuthenticationPrincipal Jwt jwt,
                                          @PathVariable Long id, @PathVariable Long medioId) {
 main
        ListaReproduccion lista = propia(id, jwt);
        Medio medio = medioRepository.findById(medioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El medio no existe"));
        if (lista.getMedios().stream().anyMatch(e -> e.getMedio().getId().equals(medioId))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El medio ya está en la lista");
        }
        ListaReproduccionMedio entrada = new ListaReproduccionMedio();
        entrada.setListaReproduccion(lista);
        entrada.setMedio(medio);
        entrada.setPosicion(lista.getMedios().size() + 1);
        entrada.setFechaAgregado(LocalDate.now());
        lista.getMedios().add(entrada);
 main
          return ResponseEntity.status(HttpStatus.CREATED).body(listaReproduccionRepository.save(lista));

        return listaReproduccionRepository.save(lista);
 main
    }


    @Operation(summary = "Quitar un medio de mi lista", description = "Reordena las posiciones restantes.")
    @DeleteMapping("/{id}/medios/{medioId}")
    public ListaReproduccion quitarMedio(@AuthenticationPrincipal Jwt jwt,
                                         @PathVariable Long id, @PathVariable Long medioId) {
        ListaReproduccion lista = propia(id, jwt);
 main
        boolean quitado = lista.getMedios().removeIf(e -> e.getMedio().getId().equals(medioId)); 

        boolean quitado = lista.getMedios().removeIf(e -> e.getMedio().getId().equals(medioId)); // orphanRemoval lo borra
 main
        if (!quitado) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El medio no está en la lista");
        }

        List<ListaReproduccionMedio> restantes = lista.getMedios().stream()
                .sorted(Comparator.comparing(ListaReproduccionMedio::getPosicion,
                        Comparator.nullsLast(Comparator.<Integer>naturalOrder())))
                .toList();
        for (int i = 0; i < restantes.size(); i++) {
            restantes.get(i).setPosicion(i + 1);
        }
        return listaReproduccionRepository.save(lista);
    }
}
