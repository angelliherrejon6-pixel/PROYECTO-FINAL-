package aspirest.controller;

import aspirest.models.Medio;
import aspirest.repo.MedioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/medios")
public class MedioController {
    @Autowired private MedioRepository medioRepository;

    @GetMapping
    public List<Medio> listar() { return medioRepository.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Medio> obtenerPorId(@PathVariable Long id) {
        return medioRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Medio crear(@RequestBody Medio medio) { return medioRepository.save(medio); }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (medioRepository.existsById(id)) {
            medioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
