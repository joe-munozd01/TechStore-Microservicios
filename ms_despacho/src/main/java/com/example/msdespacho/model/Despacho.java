package com.example.msdespacho.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "despachos")
public class Despacho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "La direccion es obligatoria")
    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String estado = "PENDIENTE"; // PENDIENTE, EN_RUTA, ENTREGADO

    private String transportista;

    @NotNull(message = "El idOrden es obligatorio")
    @Column(name = "orden_id", nullable = false)
    private Integer idOrden;

    @Column(name = "fecha_despacho")
    private LocalDateTime fechaDespacho;
}
