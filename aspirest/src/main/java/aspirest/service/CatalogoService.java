package aspirest.service;

import aspirest.decorator.*;
import aspirest.dto.CatalogoRespuesta;
import aspirest.models.Medio;
import aspirest.repo.MedioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CatalogoService {

    private final MedioRepository medioRepository;

    public CatalogoService(MedioRepository medioRepository) {
        this.medioRepository = medioRepository;
    }

    public List<String> listarGeneros() {
        return medioRepository.listarGeneros();
    }

    public CatalogoRespuesta construir(String genero, String artista, Integer desde, Integer hasta,
                                       Integer maxDuracion, String orden, boolean descendente, Integer limite) {

        if (!medioRepository.existsByGeneroIgnoreCase(genero)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el género: " + genero);
        }
        if (limite != null && limite < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El límite debe ser mayor a 0");
        }

        CatalogoGenero catalogo = new CatalogoBase(genero, medioRepository);

        // 1) Filtros
        if (artista != null && !artista.isBlank()) {
            catalogo = new FiltroArtistaDecorator(catalogo, artista.trim());
        }
        if (desde != null || hasta != null) {
            catalogo = new FiltroAnioDecorator(catalogo, desde, hasta);
        }
        if (maxDuracion != null) {
            catalogo = new FiltroDuracionDecorator(catalogo, maxDuracion);
        }
        // 2) Orden
        if (orden != null && !orden.isBlank()) {
            try {
                catalogo = new OrdenDecorator(catalogo,
                        OrdenDecorator.Criterio.valueOf(orden.trim().toUpperCase()), descendente);
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Orden no válido. Use: nombre, artista, anio o duracion");
            }
        }
        // 3) Límite
        if (limite != null) {
            catalogo = new LimiteDecorator(catalogo, limite);
        }

        List<Medio> medios = catalogo.getMedios();
        int duracionTotal = medios.stream()
                .mapToInt(m -> m.getDuracionMinutos() == null ? 0 : m.getDuracionMinutos())
                .sum();

        return new CatalogoRespuesta(catalogo.getGenero(), catalogo.getDescripcion(),
                medios.size(), duracionTotal, medios);
    }
}