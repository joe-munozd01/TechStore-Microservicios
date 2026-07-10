package com.example.msnotificacion.DTO;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class NotificacionDTO {
    private Integer id;
    private Integer idUsuario;
    private String tipo;
    private String mensaje;
    private boolean leido;
    private LocalDateTime fechaEnvio;
}
