package com.example.mspago.DTO;

import lombok.Data;

@Data
public class PagoDTO {

    private Integer id;
    private String metodoPago;
    private Integer monto;
    private Integer idOrden;
}
