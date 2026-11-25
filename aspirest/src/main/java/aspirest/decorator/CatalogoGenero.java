package aspirest.decorator;

import aspirest.models.Medio;
import java.util.List;

public interface CatalogoGenero {
    String getGenero();
    List<Medio> getMedios();
    String getDescripcion();
}
