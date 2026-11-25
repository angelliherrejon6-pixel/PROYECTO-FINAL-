package aspirest.controller;

import aspirest.models.Usuario;
import aspirest.repo.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Registro e inicio de sesión (JWT)")
public class AuthController {
       public record RegistroRequest(@NotBlank String nombre,
                                  String fechaNacimiento,
                                  @Email @NotBlank String correo,
                                  @NotBlank @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres") String contraseña) { }

    public record LoginRequest(@NotBlank String correo, @NotBlank String contraseña) { }

    public record TokenRespuesta(String token, String tipo, long expiraEnMinutos) { }

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder encoder;
    private final JwtEncoder jwtEncoder;

    @Value("${jwt.expiracion-minutos}")
    private long minutos;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder encoder, JwtEncoder jwtEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.encoder = encoder;
        this.jwtEncoder = jwtEncoder;
    }

    @Operation(summary = "Registrar un usuario",
               description = "Correo válido y único, contraseña de mínimo 8 caracteres (se guarda cifrada con BCrypt).")
    @PostMapping("/registro")
    public ResponseEntity<Usuario> registro(@Valid @RequestBody RegistroRequest r) {
        String correo = r.correo().trim().toLowerCase();
        if (usuarioRepository.findByCorreo(correo).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
        }
        Usuario u = new Usuario();
        u.setNombre(r.nombre());
        u.setFechaNacimiento(r.fechaNacimiento());
        u.setCorreo(correo);
        u.setContraseña(encoder.encode(r.contraseña()));
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRepository.save(u));
    }

    @Operation(summary = "Iniciar sesión", description = "Devuelve un JWT para usarlo como Bearer token (botón Authorize).")
    @PostMapping("/login")
    public TokenRespuesta login(@Valid @RequestBody LoginRequest r) {
        Usuario u = usuarioRepository.findByCorreo(r.correo().trim().toLowerCase())
                .filter(x -> x.getContraseña() != null && encoder.matches(r.contraseña(), x.getContraseña()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

        Instant ahora = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(u.getCorreo())
                .issuedAt(ahora)
                .expiresAt(ahora.plus(minutos, ChronoUnit.MINUTES))
                .claim("uid", u.getId())
                .build();
        // El header HS256 es obligatorio con clave simétrica
        String token = jwtEncoder.encode(
                JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();
        return new TokenRespuesta(token, "Bearer", minutos);
    } 
}
