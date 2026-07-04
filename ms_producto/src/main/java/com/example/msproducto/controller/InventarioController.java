package com.example.msproducto.controller;

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
import com.example.msproducto.DTO.InventarioDTO;
import com.example.msproducto.model.Inventario;
import com.example.msproducto.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/inventarios")
@Tag(name = "Inventarios", description = "Gestión de inventario y stock de productos")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @Operation(summary = "Listar todos los inventarios")
    @GetMapping
    public ResponseEntity<?> listarInventarios() {

        List<InventarioDTO> inventarios = inventarioService.obtenerTodos();

        if (inventarios.isEmpty()) {
            return new ResponseEntity<>("No hay inventarios", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(inventarios, HttpStatus.OK);
    }

    @Operation(summary = "Buscar inventario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarInventario(@PathVariable Integer id) {
        try {
            InventarioDTO inventario = inventarioService.buscarPorId(id);
            return new ResponseEntity<>(inventario, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Registrar nuevo inventario")
    @PostMapping
    public ResponseEntity<?> guardarInventario(@Valid @RequestBody Inventario inventario) {
        try {
            return new ResponseEntity<>(inventarioService.guardar(inventario), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("No se pudo guardar el inventario", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar inventario existente")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarInventario(@PathVariable Integer id,
                                                   @Valid @RequestBody Inventario inventario) {
        try {
            Inventario inventarioActualizado = inventarioService.actualizarInventario(id, inventario);
            return new ResponseEntity<>(inventarioActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar inventario por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarInventario(@PathVariable Integer id) {

        String resultado = inventarioService.eliminar(id);

        if (resultado.contains("correctamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
}
