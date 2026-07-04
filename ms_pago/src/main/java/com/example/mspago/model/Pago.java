package com.example.mspago.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * En el monolito original, Pago tenia un @OneToOne hacia Orden.
 * Orden ahora vive en ms-orden (otra base de datos), por lo que la
 * relacion JPA se reemplaza por un simple campo idOrden.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El método de pago es obligatorio")
    @Column(nullable = false)
    private String metodoPago;

    @Column(nullable = false)
    private Integer monto;

    @Column(name = "orden_id")
    private Integer idOrden;
}
