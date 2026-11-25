package aspirest.decorator;
import aspirest.models.Medio;
import java.util.List;


public class FiltroDuracionDecorator extends CatalogoDecorator {
 private final int maxMinutos;

    public FiltroDuracionDecorator(CatalogoGenero catalogo, int maxMinutos) {
        super(catalogo);
        this.maxMinutos = maxMinutos;
    }

    @Override
    public List<Medio> getMedios() {
        return super.getMedios().stream()
                .filter(m -> m.getDuracionMinutos() != null && m.getDuracionMinutos() <= maxMinutos)
                .toList();
    }

    @Override
    public String getDescripcion() { return super.getDescripcion() + " | duración máx: " + maxMinutos + " min"; }   
    
}
