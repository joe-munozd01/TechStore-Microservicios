package com.example.msorden.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.msorden.DTO.DetalleOrdenDTO;
import com.example.msorden.model.DetalleOrden;
import com.example.msorden.service.DetalleOrdenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/detalle-orden")
@Tag(name = "Detalle Orden", description = "Gestión de los items dentro de una orden")
public class DetalleOrdenController {

    @Autowired
    private DetalleOrdenService detalleOrdenService;

    @Operation(summary = "Listar todos los detalles de orden")
    @GetMapping
    public ResponseEntity<?> listarDetalles() {

        List<DetalleOrdenDTO> detalles = detalleOrdenService.obtenerTodos();

        if (detalles.isEmpty()) {
            return new ResponseEntity<>("No hay detalles", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(detalles, HttpStatus.OK);
    }

    @Operation(summary = "Agregar item a una orden")
    @PostMapping
    public ResponseEntity<?> guardarDetalle(@Valid @RequestBody DetalleOrden detalle) {
        try {
            return new ResponseEntity<>(detalleOrdenService.guardar(detalle), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("No se pudo guardar el detalle", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar un detalle de orden")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarDetalle(@PathVariable Integer id, @RequestBody DetalleOrden detalle) {
        try {
            return new ResponseEntity<>(detalleOrdenService.actualizar(id, detalle), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar un detalle de orden")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDetalle(@PathVariable Integer id) {
        try {
            String mensaje = detalleOrdenService.eliminar(id);
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
