package aspirest.dto;


import java.util.List;

public record ErrorRespuesta(String timestamp,
                             int status,
                             String error,
                             String mensaje,
                             String ruta,
                             List<String> detalles) { }