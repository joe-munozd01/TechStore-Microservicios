package com.example.msproducto.DTO;

import lombok.Data;

@Data
public class InventarioDTO {

    private Integer id;
    private Integer stock;
    private Integer stockMinimo;
    private String ubicacion;
    private String nombreProducto;
}
