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
import com.example.msorden.DTO.OrdenDTO;
import com.example.msorden.model.Orden;
import com.example.msorden.service.OrdenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/ordenes")
@Tag(name = "Ordenes", description = "Gestión de órdenes de compra")
public class OrdenController {

    @Autowired
    private OrdenService ordenService;

    @Operation(summary = "Listar todas las órdenes")
    @GetMapping
    public ResponseEntity<?> listarOrdenes() {

        List<OrdenDTO> ordenes = ordenService.obtenerTodas();

        if (ordenes.isEmpty()) {
            return new ResponseEntity<>("No hay órdenes", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(ordenes, HttpStatus.OK);
    }

    @Operation(summary = "Buscar orden por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarOrden(@PathVariable Integer id) {
        try {
            OrdenDTO orden = ordenService.buscarPorId(id);
            return new ResponseEntity<>(orden, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Crear nueva orden")
    @PostMapping
    public ResponseEntity<?> guardarOrden(@Valid @RequestBody Orden orden) {
        try {
            return new ResponseEntity<>(ordenService.guardar(orden), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("No se pudo guardar la orden", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar orden existente")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarOrden(@PathVariable Integer id, @Valid @RequestBody Orden orden) {
        try {
            Orden ordenActualizada = ordenService.actualizar(id, orden);
            return new ResponseEntity<>(ordenActualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar orden por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarOrden(@PathVariable Integer id) {

        String resultado = ordenService.eliminar(id);

        if (resultado.contains("correctamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
}
