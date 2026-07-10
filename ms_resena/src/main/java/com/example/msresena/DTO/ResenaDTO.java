package com.example.msresena.DTO;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ResenaDTO {
    private Integer id;
    private Integer idUsuario;
    private Integer idProducto;
    private Integer calificacion;
    private String comentario;
    private LocalDateTime fechaCreacion;
}