package com.example.msdespacho.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.msdespacho.DTO.DespachoDTO;
import com.example.msdespacho.model.Despacho;
import com.example.msdespacho.service.DespachoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/despachos")
@Tag(name = "Despachos", description = "GestiÃ³n de despachos/envÃ­os de TechStore")
public class DespachoController {

    @Autowired
    private DespachoService despachoService;

    @Operation(summary = "Listar todos los despachos")
    @GetMapping
    public ResponseEntity<?> listarDespachos() {
        List<DespachoDTO> despachos = despachoService.obtenerTodos();
        if (despachos.isEmpty()) {
            return new ResponseEntity<>("No hay despachos", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(despachos, HttpStatus.OK);
    }

    @Operation(summary = "Buscar despacho por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarDespacho(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(despachoService.buscarPorId(id), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Buscar despacho por orden")
    @GetMapping("/orden/{idOrden}")
    public ResponseEntity<?> buscarPorOrden(@PathVariable Integer idOrden) {
        return new ResponseEntity<>(despachoService.buscarPorOrden(idOrden), HttpStatus.OK);
    }

    @Operation(summary = "Crear nuevo despacho")
    @PostMapping
    public ResponseEntity<?> crearDespacho(@Valid @RequestBody Despacho despacho) {
        try {
            return new ResponseEntity<>(despachoService.guardar(despacho), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("No se pudo crear el despacho", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar despacho existente")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarDespacho(@PathVariable Integer id, @RequestBody Despacho despacho) {
        try {
            return new ResponseEntity<>(despachoService.actualizar(id, despacho), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Cambiar estado del despacho")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Integer id, @RequestParam String estado) {
        try {
            return new ResponseEntity<>(despachoService.cambiarEstado(id, estado), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar despacho por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDespacho(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(despachoService.eliminar(id), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}