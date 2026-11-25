package aspirest.controller;

import aspirest.models.Usuario;
import aspirest.repo.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Registro y gestión de usuarios")


public class UsuarioController {
    @Autowired private UsuarioRepository usuarioRepository;
     @Operation(summary = "Listar todos los usuarios",
               description = "Incluye las listas de reproducción de cada usuario. La contraseña nunca se devuelve.")
    @GetMapping
    public List<Usuario> listar() { return usuarioRepository.findAll(); }

    @Operation(summary = "Obtener un usuario por id")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        return usuarioRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Registrar un usuario",
               description = "Crea un nuevo usuario. La contraseña es solo de escritura.")
    @PostMapping
    public Usuario crear(@RequestBody Usuario usuario) { return usuarioRepository.save(usuario); }

    
     @Operation(summary = "Actualizar un usuario")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody Usuario datos) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombre(datos.getNombre());
            usuario.setFechaNacimiento(datos.getFechaNacimiento());
            usuario.setContraseña(datos.getContraseña());
            return ResponseEntity.ok(usuarioRepository.save(usuario));
        }).orElse(ResponseEntity.notFound().build());
    }



    @Operation(summary = "Eliminar un usuario",
               description = "Elimina también sus listas de reproducción (cascade). Los medios del catálogo no se eliminan.")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado", content = @Content)
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
