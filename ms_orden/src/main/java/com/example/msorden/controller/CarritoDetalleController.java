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
import com.example.msorden.DTO.CarritoDetalleDTO;
import com.example.msorden.model.CarritoDetalle;
import com.example.msorden.service.CarritoDetalleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/carrito-detalle")
@Tag(name = "Carrito Detalle", description = "Gestión de los items dentro de un carrito")
public class CarritoDetalleController {

    @Autowired
    private CarritoDetalleService carritoDetalleService;

    @Operation(summary = "Listar todos los detalles de carrito")
    @GetMapping
    public ResponseEntity<?> listarDetalles() {

        List<CarritoDetalleDTO> detalles = carritoDetalleService.obtenerTodos();

        if (detalles.isEmpty()) {
            return new ResponseEntity<>("No hay detalles", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(detalles, HttpStatus.OK);
    }

    @Operation(summary = "Agregar item a un carrito")
    @PostMapping
    public ResponseEntity<?> guardarDetalle(@Valid @RequestBody CarritoDetalle detalle) {
        try {
            return new ResponseEntity<>(carritoDetalleService.guardar(detalle), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("No se pudo guardar el detalle", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar un detalle de carrito")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarDetalle(@PathVariable Integer id, @RequestBody CarritoDetalle detalle) {
        try {
            return new ResponseEntity<>(carritoDetalleService.actualizar(id, detalle), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar un detalle de carrito")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDetalle(@PathVariable Integer id) {
        try {
            String mensaje = carritoDetalleService.eliminar(id);
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
