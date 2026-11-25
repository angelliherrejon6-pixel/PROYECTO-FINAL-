package aspirest.decorator;
import aspirest.models.Medio;
import java.util.List;

public record CatalogoRespuesta (String genero,
                                String descripcion,
                                int total,
                                int duracionTotalMinutos,
                                List<Medio> medios) { }
