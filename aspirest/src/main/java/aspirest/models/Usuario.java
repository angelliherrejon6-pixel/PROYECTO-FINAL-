package aspirest.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "usuario")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String fechaNacimiento;

    // JWT Security
    @Column(unique = true)
    private String correo;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String contraseña;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ListaReproduccion> listas = new ArrayList<>();
   
    @ManyToMany
    @JoinTable(name = "usuario_favorito",
               joinColumns = @JoinColumn(name = "usuario_id"),
               inverseJoinColumns = @JoinColumn(name = "medio_id"))
    private Set<Medio> favoritos = new HashSet<>();
    public Usuario() { }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
   
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }    
   
    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }

    public List<ListaReproduccion> getListas() { return listas; }
    public void setListas(List<ListaReproduccion> listas) { this.listas = listas; }

    public Set<Medio> getFavoritos() { return favoritos; }
    public void setFavoritos(Set<Medio> favoritos) { this.favoritos = favoritos; }
}