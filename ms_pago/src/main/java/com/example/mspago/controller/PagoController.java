package com.example.mspago.controller;

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
import com.example.mspago.DTO.PagoDTO;
import com.example.mspago.model.Pago;
import com.example.mspago.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/pagos")
@Tag(name = "Pagos", description = "Gestión de pagos de órdenes")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Operation(summary = "Listar todos los pagos")
    @GetMapping
    public ResponseEntity<?> listarPagos() {

        List<PagoDTO> pagos = pagoService.obtenerTodos();

        if (pagos.isEmpty()) {
            return new ResponseEntity<>("No hay pagos", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(pagos, HttpStatus.OK);
    }

    @Operation(summary = "Buscar pago por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPago(@PathVariable Integer id) {
        try {
            PagoDTO pago = pagoService.buscarPorId(id);
            return new ResponseEntity<>(pago, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Registrar nuevo pago")
    @PostMapping
    public ResponseEntity<?> guardarPago(@Valid @RequestBody Pago pago) {
        try {
            return new ResponseEntity<>(pagoService.guardar(pago), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("No se pudo guardar el pago", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar pago existente")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPago(@PathVariable Integer id, @RequestBody Pago pago) {
        try {
            return new ResponseEntity<>(pagoService.actualizar(id, pago), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar pago por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPago(@PathVariable Integer id) {
        try {
            String mensaje = pagoService.eliminar(id);
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
