package aspirest.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "lista_reproduccion_medio",
       uniqueConstraints = @UniqueConstraint(columnNames = {"lista_id", "medio_id"}))
public class ListaReproduccionMedio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lista_id")
    @JsonBackReference("lista-medios")
    private ListaReproduccion listaReproduccion;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "medio_id")
    private Medio medio;

    private Integer posicion;
    private LocalDate fechaAgregado;

    public ListaReproduccionMedio() { }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public ListaReproduccion getListaReproduccion() { return listaReproduccion; }
    public void setListaReproduccion(ListaReproduccion listaReproduccion) { this.listaReproduccion = listaReproduccion; }
    
    public Medio getMedio() { return medio; }
    public void setMedio(Medio medio) { this.medio = medio; }
    
    public Integer getPosicion() { return posicion; }
    public void setPosicion(Integer posicion) { this.posicion = posicion; }
    
    public LocalDate getFechaAgregado() { return fechaAgregado; }
    public void setFechaAgregado(LocalDate fechaAgregado) { this.fechaAgregado = fechaAgregado; }
}