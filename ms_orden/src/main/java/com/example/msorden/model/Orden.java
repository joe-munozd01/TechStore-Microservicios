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
 * En el monolito original, Orden tenia un @ManyToOne hacia Usuario y un
 * @OneToOne hacia Pago. Ambas entidades ahora viven en otros
 * microservicios (ms-usuario y ms-pago respectivamente), por lo que esas
 * relaciones JPA se eliminan: el usuario se referencia por idUsuario, y
 * es ms-pago quien guarda su propio idOrden apuntando hacia aca.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ordenes")
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer total;

    @Column(name = "usuario_id")
    private Integer idUsuario;

    @OneToMany(mappedBy = "orden")
    private List<DetalleOrden> detalles;
}
