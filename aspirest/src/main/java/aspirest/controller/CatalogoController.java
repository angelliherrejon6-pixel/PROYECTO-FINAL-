package aspirest.controller;
import aspirest.dto.CatalogoRespuesta;
import aspirest.service.CatalogoService;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;


@RestController
@RequestMapping("/api/catalogo")
@Tag(name = "Catálogo por géneros", description = "Catálogos de un género extendidos dinámicamente con el patrón Decorator")
public class CatalogoController {

    private final CatalogoService catalogoService;

    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @Operation(summary = "Listar los géneros disponibles")
    @GetMapping("/generos")
    public List<String> generos() {
        return catalogoService.listarGeneros();
    }

    @Operation(summary = "Consultar el catálogo de un género",
               description = "Cada parámetro opcional agrega un decorador al catálogo base: "
                           + "artista, desde/hasta (año), maxDuracion (minutos), "
                           + "orden (nombre, artista, anio, duracion) y limite.")
    @GetMapping("/{genero}")
    public CatalogoRespuesta catalogo(@PathVariable String genero,
                                      @RequestParam(required = false) String artista,
                                      @RequestParam(required = false) Integer desde,
                                      @RequestParam(required = false) Integer hasta,
                                      @RequestParam(required = false) Integer maxDuracion,
                                      @RequestParam(required = false) String orden,
                                      @RequestParam(defaultValue = "false") boolean descendente,
                                      @RequestParam(required = false) Integer limite) {
        return catalogoService.construir(genero, artista, desde, hasta, maxDuracion, orden, descendente, limite);
    }
}
