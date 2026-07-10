package com.example.mssoporte.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El asunto es obligatorio")
    @Size(min = 3, max = 150)
    @Column(nullable = false, length = 150)
    private String asunto;

    @NotBlank(message = "La descripcion es obligatoria")
    @Size(min = 3, max = 1000)
    @Column(nullable = false, length = 1000)
    private String descripcion;

    @Column(nullable = false)
    private String estado = "ABIERTO"; 

    @Column(nullable = false)
    private String prioridad = "MEDIA"; 

    @NotNull(message = "El idUsuario es obligatorio")
    @Column(name = "usuario_id", nullable = false)
    private Integer idUsuario;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}
