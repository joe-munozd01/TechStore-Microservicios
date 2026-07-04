package com.example.msorden.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * En el monolito original, Carrito tenia un @ManyToOne hacia Usuario.
 * Usuario ahora vive en ms-usuario (otra base de datos), por lo que
 * la relacion JPA se reemplaza por un simple campo idUsuario.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "carritos")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "usuario_id")
    private Integer idUsuario;

    @OneToMany(mappedBy = "carrito")
    private List<CarritoDetalle> detalles;
}
