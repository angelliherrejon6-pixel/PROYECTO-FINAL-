package aspirest.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "medios")
public class Medio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String genero;
    private String artista;
    private String album;
    private Integer duracionMinutos;
    private Integer añoLanzamiento;

    public Medio() { }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public String getArtista() { return artista; }
    public void setArtista(String artista) { this.artista = artista; }
    public String getAlbum() { return album; }
    public void setAlbum(String album) { this.album = album; }
    public Integer getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(Integer duracionMinutos) { this.duracionMinutos = duracionMinutos; }
    public Integer getAñoLanzamiento() { return añoLanzamiento; }
    public void setAñoLanzamiento(Integer añoLanzamiento) { this.añoLanzamiento = añoLanzamiento; }
}
