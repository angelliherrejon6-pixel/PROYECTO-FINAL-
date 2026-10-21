package aspirest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import aspirest.models.Cancion;
import aspirest.repo.CancionRepository;

@RestController
public class CancionController {
    private final CancionRepository repo;

    public CancionController(CancionRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/canciones")
    public List<Cancion> findAllCanciones() {
        return repo.findAll();
    }

    @PostMapping("/canciones")
    public Cancion addCancion(@RequestBody Cancion cancion) {
        return repo.save(cancion);
    }
}
