package aspirest.decorator;

import aspirest.models.Medio;

import java.util.List;

public abstract class CatalogoDecorator implements CatalogoGenero {

    protected final CatalogoGenero catalogo;

    protected CatalogoDecorator(CatalogoGenero catalogo) {
        this.catalogo = catalogo;
    }

    @Override
    public String getGenero() { return catalogo.getGenero(); }

    @Override
    public List<Medio> getMedios() { return catalogo.getMedios(); }

    @Override
    public String getDescripcion() { return catalogo.getDescripcion(); }
}
