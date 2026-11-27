package aspirest.controller;

import aspirest.models.Medio;
import aspirest.repo.MedioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.net.URI;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/medios")
@Tag(name = "Medios", description = "Catálogo de canciones")
public class MedioController {
    @Autowired private MedioRepository medioRepository;

    @Operation(summary = "Listar todos los medios")
    @GetMapping
    public List<Medio> listar() { return medioRepository.findAll(); }

    @Operation(summary = "Obtener un medio por su id")
    @ApiResponse(responseCode = "404", description = "Medio no encontrado", content = @io.swagger.v3.oas.annotations.media.Content)
    @GetMapping("/{id}")
    public ResponseEntity<Medio> obtenerPorId(@PathVariable Long id) {
        return medioRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un medio", description = "Devuelve 201 con la cabecera Location del nuevo medio.")
    @ApiResponse(responseCode = "201", description = "Medio creado")
    @PostMapping
    public ResponseEntity<Medio> crear(@RequestBody Medio medio) {
        medio.setId(null); // evita que un POST con id sobrescriba un medio existente
        Medio guardado = medioRepository.save(medio);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(guardado.getId()).toUri();
        return ResponseEntity.created(ubicacion).body(guardado);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Medio> actualizar(@PathVariable Long id, @RequestBody Medio datos) {
        return medioRepository.findById(id).map(medio -> {
            medio.setNombre(datos.getNombre());
            medio.setGenero(datos.getGenero());
            medio.setArtista(datos.getArtista());
            medio.setAlbum(datos.getAlbum());
            medio.setDuracionMinutos(datos.getDuracionMinutos());
            medio.setAñoLanzamiento(datos.getAñoLanzamiento());
            return ResponseEntity.ok(medioRepository.save(medio));
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un medio")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (medioRepository.existsById(id)) {
            medioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
