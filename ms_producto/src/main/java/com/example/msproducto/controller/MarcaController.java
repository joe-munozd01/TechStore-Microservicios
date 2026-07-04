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
import com.example.msproducto.DTO.MarcaDTO;
import com.example.msproducto.model.Marca;
import com.example.msproducto.service.MarcaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/marcas")
@Tag(name = "Marcas", description = "Gestión de marcas de productos")
public class MarcaController {

    @Autowired
    private MarcaService marcaService;

    @Operation(summary = "Listar todas las marcas")
    @GetMapping
    public ResponseEntity<?> listarMarcas() {

        List<MarcaDTO> marcas = marcaService.obtenerTodas();

        if (marcas.isEmpty()) {
            return new ResponseEntity<>("No hay marcas", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(marcas, HttpStatus.OK);
    }

    @Operation(summary = "Buscar marca por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarMarca(@PathVariable Integer id) {
        try {
            MarcaDTO marca = marcaService.buscarPorId(id);
            return new ResponseEntity<>(marca, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Registrar nueva marca")
    @PostMapping
    public ResponseEntity<?> guardarMarca(@Valid @RequestBody Marca marca) {
        try {
            return new ResponseEntity<>(marcaService.guardar(marca), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("No se pudo guardar la marca", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar marca existente")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarMarca(@PathVariable Integer id, @RequestBody Marca marca) {
        try {
            return new ResponseEntity<>(marcaService.actualizar(id, marca), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar marca por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMarca(@PathVariable Integer id) {
        try {
            String mensaje = marcaService.eliminar(id);
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
