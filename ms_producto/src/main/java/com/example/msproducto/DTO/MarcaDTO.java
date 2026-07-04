package com.example.msproducto.DTO;

import java.util.List;
import lombok.Data;

@Data
public class MarcaDTO {
    private Integer id;
    private String nombre;
    private List<String> nombresProductos;
}
