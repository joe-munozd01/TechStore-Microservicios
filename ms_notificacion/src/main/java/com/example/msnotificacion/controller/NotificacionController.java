package com.example.msnotificacion.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.msnotificacion.DTO.NotificacionDTO;
import com.example.msnotificacion.model.Notificacion;
import com.example.msnotificacion.service.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/notificaciones")
@Tag(name = "Notificaciones", description = "GestiÃ³n de notificaciones de TechStore")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @Operation(summary = "Listar todas las notificaciones")
    @GetMapping
    public ResponseEntity<?> listarNotificaciones() {
        List<NotificacionDTO> notificaciones = notificacionService.obtenerTodas();
        if (notificaciones.isEmpty()) {
            return new ResponseEntity<>("No hay notificaciones", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(notificaciones, HttpStatus.OK);
    }

    @Operation(summary = "Buscar notificacion por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarNotificacion(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(notificacionService.buscarPorId(id), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Listar notificaciones de un usuario")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> buscarPorUsuario(@PathVariable Integer idUsuario) {
        return new ResponseEntity<>(notificacionService.buscarPorUsuario(idUsuario), HttpStatus.OK);
    }

    @Operation(summary = "Crear nueva notificacion")
    @PostMapping
    public ResponseEntity<?> crearNotificacion(@Valid @RequestBody Notificacion notificacion) {
        try {
            return new ResponseEntity<>(notificacionService.guardar(notificacion), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("No se pudo crear la notificacion", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Marcar notificacion como leida")
    @PatchMapping("/{id}/leido")
    public ResponseEntity<?> marcarLeido(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(notificacionService.marcarLeido(id), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar notificacion por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarNotificacion(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(notificacionService.eliminar(id), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
