package aspirest.controller;

import aspirest.models.ListaReproduccion;
import aspirest.repo.ListaReproduccionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listas")
public class ListaReproduccionController {

    private final ListaReproduccionRepository listaReproduccionRepository;

    public ListaReproduccionController(ListaReproduccionRepository listaReproduccionRepository) {
        this.listaReproduccionRepository = listaReproduccionRepository;
    }

    @GetMapping
    public List<ListaReproduccion> listar() {
        return listaReproduccionRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListaReproduccion> obtener(@PathVariable Long id) {
        return listaReproduccionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ListaReproduccion> crear(@RequestBody ListaReproduccion lista) {
        lista.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(listaReproduccionRepository.save(lista));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListaReproduccion> actualizar(@PathVariable Long id, @RequestBody ListaReproduccion datos) {
        return listaReproduccionRepository.findById(id).map(lista -> {
            lista.setNombre(datos.getNombre());
            lista.setDuracionMinutos(datos.getDuracionMinutos());
            lista.setURL(datos.getURL());
            
            if (datos.getUsuario() != null) {
                lista.setUsuario(datos.getUsuario());
            }
            
            return ResponseEntity.ok(listaReproduccionRepository.save(lista));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!listaReproduccionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        listaReproduccionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}