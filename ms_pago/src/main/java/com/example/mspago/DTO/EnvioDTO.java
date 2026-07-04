package com.example.mspago.DTO;

import lombok.Data;

@Data
public class EnvioDTO {
    private Integer id;
    private String direccion;
    private String estado;
    private String codigoSeguimiento;
    private Integer idOrden;
}
