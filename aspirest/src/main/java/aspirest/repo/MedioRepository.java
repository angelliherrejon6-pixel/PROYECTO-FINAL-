package aspirest.repo;

import aspirest.models.Medio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MedioRepository extends JpaRepository<Medio, Long> {
 @Query("""
        select m from Medio m
        where lower(coalesce(m.nombre, ''))  like lower(concat('%', :nombre, '%'))
          and lower(coalesce(m.artista, '')) like lower(concat('%', :artista, '%'))
          and lower(coalesce(m.album, ''))   like lower(concat('%', :album, '%'))
          and lower(coalesce(m.genero, ''))  like lower(concat('%', :genero, '%'))
        order by m.nombre
        """)
    List<Medio> buscar(@Param("nombre") String nombre,
                       @Param("artista") String artista,
                       @Param("album") String album,
                       @Param("genero") String genero);

    // Búsqueda libre
    @Query("""
        select m from Medio m
        where lower(coalesce(m.nombre, ''))  like lower(concat('%', :q, '%'))
           or lower(coalesce(m.artista, '')) like lower(concat('%', :q, '%'))
           or lower(coalesce(m.album, ''))   like lower(concat('%', :q, '%'))
           or lower(coalesce(m.genero, ''))  like lower(concat('%', :q, '%'))
        order by m.nombre
        """)
    List<Medio> buscarLibre(@Param("q") String q);

    // Soporte del catálogo por géneros
    List<Medio> findByGeneroIgnoreCase(String genero);

    boolean existsByGeneroIgnoreCase(String genero);

    @Query("select distinct m.genero from Medio m where m.genero is not null order by m.genero")
    List<String> listarGeneros();
}
