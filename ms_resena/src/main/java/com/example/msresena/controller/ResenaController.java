package com.example.msresena.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.msresena.DTO.ResenaDTO;
import com.example.msresena.model.Resena;
import com.example.msresena.service.ResenaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/resenas")
@Tag(name = "Reseñas", description = "Gestión de reseñas de productos de TechStore")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    @Operation(summary = "Listar todas las reseñas")
    @GetMapping
    public ResponseEntity<?> listarResenas() {
        List<ResenaDTO> resenas = resenaService.obtenerTodas();
        if (resenas.isEmpty()) {
            return new ResponseEntity<>("No hay reseñas", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(resenas, HttpStatus.OK);
    }

    @Operation(summary = "Buscar reseña por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarResena(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(resenaService.buscarPorId(id), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Listar reseñas de un producto")
    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<?> buscarPorProducto(@PathVariable Integer idProducto) {
        return new ResponseEntity<>(resenaService.buscarPorProducto(idProducto), HttpStatus.OK);
    }

    @Operation(summary = "Listar reseñas de un usuario")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> buscarPorUsuario(@PathVariable Integer idUsuario) {
        return new ResponseEntity<>(resenaService.buscarPorUsuario(idUsuario), HttpStatus.OK);
    }

    @Operation(summary = "Crear nueva reseña")
    @PostMapping
    public ResponseEntity<?> crearResena(@Valid @RequestBody Resena resena) {
        try {
            return new ResponseEntity<>(resenaService.guardar(resena), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("No se pudo crear la reseña", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar reseña existente")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarResena(@PathVariable Integer id, @RequestBody Resena resena) {
        try {
            return new ResponseEntity<>(resenaService.actualizar(id, resena), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar reseña por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarResena(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(resenaService.eliminar(id), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
