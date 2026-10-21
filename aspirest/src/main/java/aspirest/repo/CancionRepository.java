package aspirest.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import aspirest.models.Cancion;

public interface CancionRepository extends JpaRepository<Cancion, Long> {
}
