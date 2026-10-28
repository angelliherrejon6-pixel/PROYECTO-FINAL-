package aspirest.controller;

import aspirest.models.ListaReproduccion;
import aspirest.repo.ListaReproduccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/listas-reproduccion")
public class ListaReproduccionController {
    @Autowired private ListaReproduccionRepository listaReproduccionRepository;

    @GetMapping
    public List<ListaReproduccion> listar() { return listaReproduccionRepository.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<ListaReproduccion> obtenerPorId(@PathVariable Long id) {
        return listaReproduccionRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ListaReproduccion crear(@RequestBody ListaReproduccion lista) { return listaReproduccionRepository.save(lista); }

    @PutMapping("/{id}")
    public ResponseEntity<ListaReproduccion> actualizar(@PathVariable Long id, @RequestBody ListaReproduccion datos) {
        return listaReproduccionRepository.findById(id).map(lista -> {
            lista.setNombre(datos.getNombre());
            lista.setNombreUsuario(datos.getNombreUsuario());
            lista.setDuracionMinutos(datos.getDuracionMinutos());
            lista.setURL(datos.getURL());
            return ResponseEntity.ok(listaReproduccionRepository.save(lista));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (listaReproduccionRepository.existsById(id)) {
            listaReproduccionRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
