package aspirest.controller;

import aspirest.models.Usuario;
import aspirest.repo.MedioRepository;
import aspirest.repo.UsuarioRepository;
import aspirest.models.Medio;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Registro y gestión de usuarios")

public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final MedioRepository medioRepository;
    private final PasswordEncoder encoder;

    public UsuarioController(UsuarioRepository usuarioRepository,
                             MedioRepository medioRepository,
                             PasswordEncoder encoder) {
        this.usuarioRepository = usuarioRepository;
        this.medioRepository = medioRepository;
        this.encoder = encoder;
    }

    private void exigirPropietario(Jwt jwt, Long id) {
        Number uid = jwt.getClaim("uid");
        if (uid == null || uid.longValue() != id) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes modificar a otro usuario");
        }
    }

    @Operation(summary = "Listar todos los usuarios",
               description = "Incluye listas y favoritos de cada usuario. La contraseña nunca se devuelve.")
    @GetMapping
    public List<Usuario> listar() { return usuarioRepository.findAll(); }

    @Operation(summary = "Obtener el usuario con sesión iniciada",
               description = "Devuelve el usuario del token, con sus listas y favoritos.")
    @GetMapping("/me")
    public ResponseEntity<Usuario> yo(@AuthenticationPrincipal Jwt jwt) {
        Number uid = jwt.getClaim("uid");
        return usuarioRepository.findById(uid.longValue())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener un usuario por id")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        return usuarioRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar un usuario",
               description = "Solo el propio usuario. Si se envía contraseña, se cifra; si no, se conserva la actual.")
    @ApiResponse(responseCode = "403", description = "No es tu cuenta", content = @Content)
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@AuthenticationPrincipal Jwt jwt,
                                              @PathVariable Long id,
                                              @RequestBody Usuario datos) {
        exigirPropietario(jwt, id);
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombre(datos.getNombre());
            usuario.setFechaNacimiento(datos.getFechaNacimiento());
            if (datos.getCorreo() != null && !datos.getCorreo().isBlank()) {
                String correo = datos.getCorreo().trim().toLowerCase();
                usuarioRepository.findByCorreo(correo)
                        .filter(otro -> !otro.getId().equals(id))
                        .ifPresent(otro -> {
                            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
                        });
                usuario.setCorreo(correo);
            }
            if (datos.getContraseña() != null && !datos.getContraseña().isBlank()) {
                usuario.setContraseña(encoder.encode(datos.getContraseña()));
            }
            return ResponseEntity.ok(usuarioRepository.save(usuario));
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un usuario",
               description = "Solo el propio usuario. Elimina también sus listas (cascade). Los medios del catálogo no se eliminan.")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado", content = @Content)
    @ApiResponse(responseCode = "403", description = "No es tu cuenta", content = @Content)
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        exigirPropietario(jwt, id);
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ---------- Relación muchos a muchos: favoritos ----------

    @Operation(summary = "Agregar un medio a favoritos",
               description = "Relación muchos a muchos Usuario ↔ Medio. Es idempotente: repetirlo no duplica.")
    @PostMapping("/{id}/favoritos/{medioId}")
    public ResponseEntity<Usuario> agregarFavorito(@AuthenticationPrincipal Jwt jwt,
                                                   @PathVariable Long id,
                                                   @PathVariable Long medioId) {
        exigirPropietario(jwt, id);
        Usuario u = usuarioRepository.findById(id).orElse(null);
        Medio m = medioRepository.findById(medioId).orElse(null);
        if (u == null || m == null) return ResponseEntity.notFound().build();
        u.getFavoritos().add(m);
        return ResponseEntity.ok(usuarioRepository.save(u));
    }

    @Operation(summary = "Quitar un medio de favoritos")
    @DeleteMapping("/{id}/favoritos/{medioId}")
    public ResponseEntity<Usuario> quitarFavorito(@AuthenticationPrincipal Jwt jwt,
                                                  @PathVariable Long id,
                                                  @PathVariable Long medioId) {
        exigirPropietario(jwt, id);
        Usuario u = usuarioRepository.findById(id).orElse(null);
        if (u == null) return ResponseEntity.notFound().build();
        u.getFavoritos().removeIf(m -> m.getId().equals(medioId));
        return ResponseEntity.ok(usuarioRepository.save(u));
    }
}

