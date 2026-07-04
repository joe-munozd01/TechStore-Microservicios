package com.example.msorden.DTO;

import java.util.List;
import lombok.Data;

@Data
public class CarritoDTO {

    private Integer id;
    private Integer idUsuario;
    private List<Integer> idsProductos;
}
