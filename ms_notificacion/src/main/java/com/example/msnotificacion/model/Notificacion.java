package com.example.msnotificacion.model;

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
@Table(name = "notificaciones")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "El idUsuario es obligatorio")
    @Column(name = "usuario_id", nullable = false)
    private Integer idUsuario;

    @NotBlank(message = "El tipo es obligatorio")
    @Column(nullable = false)
    private String tipo; // EMAIL, SMS, PUSH

    @NotBlank(message = "El mensaje es obligatorio")
    @Size(min = 3, max = 500)
    @Column(nullable = false, length = 500)
    private String mensaje;

    @Column(nullable = false)
    private boolean leido = false;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio = LocalDateTime.now();
}
