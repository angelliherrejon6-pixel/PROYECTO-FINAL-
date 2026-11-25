package aspirest.decorator;
import aspirest.models.Medio;
import java.util.List;

public class LimiteDecorator extends CatalogoDecorator {
 private final int limite;

    public LimiteDecorator(CatalogoGenero catalogo, int limite) {
        super(catalogo);
        this.limite = limite;
    }

    @Override
    public List<Medio> getMedios() {
        return super.getMedios().stream().limit(limite).toList();
    }

    @Override
    public String getDescripcion() { return super.getDescripcion() + " | límite: " + limite; }
}   

