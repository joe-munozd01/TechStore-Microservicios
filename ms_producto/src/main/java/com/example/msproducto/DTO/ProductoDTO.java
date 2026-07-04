package com.example.msproducto.DTO;

import lombok.Data;

@Data
public class ProductoDTO {
    private Integer id;
    private String nombre;
    private String descripcion;
    private Integer precio;
    private Integer idCategoria;
    private Integer idMarca;
    private String nombreCategoria;
    private String nombreMarca;
}
