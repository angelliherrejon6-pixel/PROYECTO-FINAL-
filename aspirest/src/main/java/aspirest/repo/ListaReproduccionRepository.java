package aspirest.repo;

import aspirest.models.ListaReproduccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;  

public interface ListaReproduccionRepository extends JpaRepository<ListaReproduccion, Long> {
    List<ListaReproduccion> findByUsuarioId(Long usuarioId); 
 }
