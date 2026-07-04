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
import com.example.msorden.DTO.CarritoDTO;
import com.example.msorden.model.Carrito;
import com.example.msorden.service.CarritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/carritos")
@Tag(name = "Carritos", description = "Gestión de carritos de compra")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Operation(summary = "Listar todos los carritos")
    @GetMapping
    public ResponseEntity<?> listarCarritos() {

        List<CarritoDTO> carritos = carritoService.obtenerTodos();

        if (carritos.isEmpty()) {
            return new ResponseEntity<>("No hay carritos", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(carritos, HttpStatus.OK);
    }

    @Operation(summary = "Buscar carrito por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarCarrito(@PathVariable Integer id) {
        try {
            CarritoDTO carrito = carritoService.buscarPorId(id);
            return new ResponseEntity<>(carrito, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Crear nuevo carrito")
    @PostMapping
    public ResponseEntity<?> guardarCarrito(@Valid @RequestBody Carrito carrito) {
        try {
            return new ResponseEntity<>(carritoService.guardar(carrito), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("No se pudo guardar el carrito", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar carrito existente")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCarrito(@PathVariable Integer id, @RequestBody Carrito carrito) {
        try {
            Carrito carritoActualizado = carritoService.actualizar(id, carrito);
            return new ResponseEntity<>(carritoActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar carrito por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCarrito(@PathVariable Integer id) {

        String resultado = carritoService.eliminar(id);

        if (resultado.contains("correctamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
}
