package aspirest.decorator;
import aspirest.models.Medio;
import java.util.List;


public class FiltroArtistaDecorator extends CatalogoDecorator {

    private final String artista;

    public FiltroArtistaDecorator(CatalogoGenero catalogo, String artista) {
        super(catalogo);
        this.artista = artista;
    }

    @Override
    public List<Medio> getMedios() {
        return super.getMedios().stream()
                .filter(medio -> artista.equalsIgnoreCase(medio.getArtista()))
                .toList();
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " filtrado por artista: " + artista;}
    }
    

