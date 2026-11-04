package aspirest.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ListaReproduccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Integer duracionMinutos;
    private String URL;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "listaReproduccion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("lista-medios")
    private List<ListaReproduccionMedio> medios = new ArrayList<>();

    public ListaReproduccion() { }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public Integer getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(Integer duracionMinutos) { this.duracionMinutos = duracionMinutos; }
    
    public String getURL() { return URL; }
    public void setURL(String URL) { this.URL = URL; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public List<ListaReproduccionMedio> getMedios() { return medios; }
    public void setMedios(List<ListaReproduccionMedio> medios) { this.medios = medios; }
}