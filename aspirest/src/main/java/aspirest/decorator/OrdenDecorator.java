package aspirest.decorator;

import aspirest.models.Medio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OrdenDecorator extends CatalogoDecorator {

    public enum Criterio { NOMBRE, ARTISTA, ANIO, DURACION }

    private final Criterio criterio;
    private final boolean descendente;

    public OrdenDecorator(CatalogoGenero catalogo, Criterio criterio, boolean descendente) {
        super(catalogo);
        this.criterio = criterio;
        this.descendente = descendente;
    }

    @Override
    public List<Medio> getMedios() {
        Comparator<Medio> comparador = switch (criterio) {
            case NOMBRE -> Comparator.comparing(Medio::getNombre,
                    Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
            case ARTISTA -> Comparator.comparing(Medio::getArtista,
                    Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
            case ANIO -> Comparator.comparing(Medio::getAñoLanzamiento,
                    Comparator.nullsLast(Comparator.<Integer>naturalOrder()));
            case DURACION -> Comparator.comparing(Medio::getDuracionMinutos,
                    Comparator.nullsLast(Comparator.<Integer>naturalOrder()));
        };
        if (descendente) comparador = comparador.reversed();

        List<Medio> medios = new ArrayList<>(super.getMedios());
        medios.sort(comparador);
        return medios;
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " | orden: " + criterio.name().toLowerCase() + (descendente ? " desc" : " asc");
    }
}