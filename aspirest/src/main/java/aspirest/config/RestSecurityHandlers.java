package aspirest.config;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

/** Responde 401 y 403 en JSON (con el mismo formato que GlobalExceptionHandler). */
@Component
public class RestSecurityHandlers implements AuthenticationEntryPoint, AccessDeniedHandler {

    // 401: sin token, token inválido o expirado
    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res,
                         AuthenticationException ex) throws IOException {
        boolean tokenInvalido = ex instanceof InvalidBearerTokenException;
        res.setHeader(HttpHeaders.WWW_AUTHENTICATE,
                tokenInvalido ? "Bearer error=\"invalid_token\"" : "Bearer");
        escribir(req, res, 401, "Unauthorized",
                tokenInvalido ? "Token inválido o expirado" : "Se requiere iniciar sesión (token Bearer)");
    }

    // 403: autenticado pero sin permiso
    @Override
    public void handle(HttpServletRequest req, HttpServletResponse res,
                       AccessDeniedException ex) throws IOException {
        escribir(req, res, 403, "Forbidden", "No tienes permiso para acceder a este recurso");
    }

    private void escribir(HttpServletRequest req, HttpServletResponse res,
                          int status, String error, String mensaje) throws IOException {
        res.setStatus(status);
        res.setContentType("application/json;charset=UTF-8");
        String json = "{\"timestamp\":\"" + Instant.now() + "\","
                + "\"status\":" + status + ","
                + "\"error\":\"" + esc(error) + "\","
                + "\"mensaje\":\"" + esc(mensaje) + "\","
                + "\"ruta\":\"" + esc(req.getRequestURI()) + "\","
                + "\"detalles\":[]}";
        res.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
    }

    private String esc(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
