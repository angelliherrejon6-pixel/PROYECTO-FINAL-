package aspirest.controller;

import aspirest.models.Medio;
import aspirest.repo.MedioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buscar")
@Tag(name = "Búsquedas", description = "Búsqueda avanzada de canciones, artistas, álbumes y géneros")
public class BusquedaController {
    private final MedioRepository medioRepository;

    public BusquedaController(MedioRepository medioRepository) {
        this.medioRepository = medioRepository;
    }

    @Operation(summary = "Buscar medios",
               description = "Si se envía `q`, busca ese texto en nombre, artista, álbum o género (OR). "
                           + "Si no, combina los filtros `nombre`, `artista`, `album` y `genero` (AND). "
                           + "Todas las coincidencias son parciales y sin distinguir mayúsculas.")
    @GetMapping
    public List<Medio> buscar(@RequestParam(required = false) String q,
                              @RequestParam(required = false) String nombre,
                              @RequestParam(required = false) String artista,
                              @RequestParam(required = false) String album,
                              @RequestParam(required = false) String genero) {
        if (q != null && !q.isBlank()) {
            return medioRepository.buscarLibre(q.trim());
        }
        return medioRepository.buscar(limpiar(nombre), limpiar(artista), limpiar(album), limpiar(genero));
    }

    private String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
