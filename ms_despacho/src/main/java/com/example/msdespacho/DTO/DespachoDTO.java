package com.example.msdespacho.DTO;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class DespachoDTO {
    private Integer id;
    private String direccion;
    private String estado;
    private String transportista;
    private Integer idOrden;
    private LocalDateTime fechaDespacho;
}
