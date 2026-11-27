package aspirest.exception;

import aspirest.dto.ErrorRespuesta;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 400: validaciones de @Valid (registro, login...)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRespuesta> validacion(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<String> detalles = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .toList();
        return construir(HttpStatus.BAD_REQUEST, "Datos inválidos", req, detalles);
    }

    // 400: JSON mal formado o tipos incorrectos en el cuerpo
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorRespuesta> jsonInvalido(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return construir(HttpStatus.BAD_REQUEST, "El cuerpo de la petición no es un JSON válido", req, List.of());
    }

    // 400: parámetro de ruta/query con tipo incorrecto (ej. /api/medios/abc)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorRespuesta> tipoIncorrecto(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        return construir(HttpStatus.BAD_REQUEST,
                "El parámetro '" + ex.getName() + "' tiene un valor inválido", req, List.of());
    }

    // 409: restricciones de BD (correo duplicado, medio repetido en lista, medio en uso al borrarlo...)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorRespuesta> integridad(DataIntegrityViolationException ex, HttpServletRequest req) {
        log.warn("Violación de integridad: {}", ex.getMostSpecificCause().getMessage());
        return construir(HttpStatus.CONFLICT,
                "La operación viola una restricción de datos (registro duplicado o en uso)", req, List.of());
    }

    // Los ResponseStatusException que ya lanzan los controladores (404, 403, 409...)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorRespuesta> estado(ResponseStatusException ex, HttpServletRequest req) {
        return construir(ex.getStatusCode(), ex.getReason(), req, List.of());
    }

    // Resto: excepciones de Spring con su propio código (404 ruta, 405, 415...) o 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRespuesta> general(Exception ex, HttpServletRequest req) {
        if (ex instanceof ErrorResponse er) {
            return construir(er.getStatusCode(), er.getBody().getDetail(), req, List.of());
        }
        log.error("Error no controlado en {}", req.getRequestURI(), ex);
        return construir(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", req, List.of());
    }

    private ResponseEntity<ErrorRespuesta> construir(HttpStatusCode codigo, String mensaje,
                                                     HttpServletRequest req, List<String> detalles) {
        HttpStatus s = HttpStatus.resolve(codigo.value());
        ErrorRespuesta cuerpo = new ErrorRespuesta(
                Instant.now().toString(),
                codigo.value(),
                s != null ? s.getReasonPhrase() : "Error",
                mensaje,
                req.getRequestURI(),
                detalles);
        return ResponseEntity.status(codigo).body(cuerpo);
    }
}
