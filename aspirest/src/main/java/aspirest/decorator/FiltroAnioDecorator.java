package aspirest.decorator;

import aspirest.models.Medio;
import java.util.List;

public class FiltroAnioDecorator extends CatalogoDecorator {

    private final Integer desde;
    private final Integer hasta;

    public FiltroAnioDecorator(CatalogoGenero catalogo, Integer desde, Integer hasta) {
        super(catalogo);
        this.desde = desde;
        this.hasta = hasta;
    }

    @Override
    public List<Medio> getMedios() {
        return super.getMedios().stream()
                .filter(m -> m.getAñoLanzamiento() != null)
                .filter(m -> desde == null || m.getAñoLanzamiento() >= desde)
                .filter(m -> hasta == null || m.getAñoLanzamiento() <= hasta)
                .toList();
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " | año: " + (desde != null ? desde : "...") + " a " + (hasta != null ? hasta : "...");
    }
}