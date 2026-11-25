package aspirest.decorator
;

import aspirest.models.Medio;
import aspirest.repo.MedioRepository;

import java.util.ArrayList;
import java.util.List;


public class CatalogoBase implements CatalogoGenero {

    private final String genero;
    private final MedioRepository medioRepository;

    public CatalogoBase(String genero, MedioRepository medioRepository) {
        this.genero = genero;
        this.medioRepository = medioRepository;
    }

    @Override
    public String getGenero() { return genero; }

    @Override
    public List<Medio> getMedios() {
        // Copia mutable para que los decoradores puedan filtrar u ordenar
        return new ArrayList<>(medioRepository.findByGeneroIgnoreCase(genero));
    }

    @Override
    public String getDescripcion() { return "Catálogo de " + genero; }
}