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
import com.example.mspago.DTO.EnvioDTO;
import com.example.mspago.model.Envio;
import com.example.mspago.service.EnvioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/envios")
@Tag(name = "Envios", description = "Gestión de envíos de órdenes")
public class EnvioController {

    @Autowired
    private EnvioService envioService;

    @Operation(summary = "Listar todos los envíos")
    @GetMapping
    public ResponseEntity<?> listarEnvios() {

        List<EnvioDTO> envios = envioService.obtenerTodos();

        if (envios.isEmpty()) {
            return new ResponseEntity<>("No hay envíos", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(envios, HttpStatus.OK);
    }

    @Operation(summary = "Buscar envío por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarEnvio(@PathVariable Integer id) {
        try {
            EnvioDTO envio = envioService.buscarPorId(id);
            return new ResponseEntity<>(envio, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Registrar nuevo envío")
    @PostMapping
    public ResponseEntity<?> guardarEnvio(@Valid @RequestBody Envio envio) {
        try {
            return new ResponseEntity<>(envioService.guardar(envio), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar envío existente")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEnvio(@PathVariable Integer id, @RequestBody Envio envio) {
        try {
            return new ResponseEntity<>(envioService.actualizar(id, envio), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar envío por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEnvio(@PathVariable Integer id) {
        try {
            String mensaje = envioService.eliminar(id);
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
