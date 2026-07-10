package com.example.mssoporte.DTO;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TicketDTO {
    private Integer id;
    private String asunto;
    private String descripcion;
    private String estado;
    private String prioridad;
    private Integer idUsuario;
    private LocalDateTime fechaCreacion;
}
