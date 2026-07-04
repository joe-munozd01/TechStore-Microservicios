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
import com.example.msproducto.DTO.CategoriaDTO;
import com.example.msproducto.model.Categoria;
import com.example.msproducto.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/categorias")
@Tag(name = "Categorias", description = "Gestión de categorías de productos")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Operation(summary = "Listar todas las categorías")
    @GetMapping
    public ResponseEntity<?> listarCategorias() {

        List<CategoriaDTO> categorias = categoriaService.obtenerTodas();

        if (categorias.isEmpty()) {
            return new ResponseEntity<>("No hay categorías", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }

    @Operation(summary = "Buscar categoría por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarCategoria(@PathVariable Integer id) {
        try {
            CategoriaDTO categoria = categoriaService.buscarPorId(id);
            return new ResponseEntity<>(categoria, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Registrar nueva categoría")
    @PostMapping
    public ResponseEntity<?> guardarCategoria(@Valid @RequestBody Categoria categoria) {
        try {
            return new ResponseEntity<>(categoriaService.guardar(categoria), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("No se pudo guardar la categoría", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar categoría existente")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCategoria(@PathVariable Integer id, @RequestBody Categoria categoria) {
        try {
            return new ResponseEntity<>(categoriaService.actualizar(id, categoria), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar categoría por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Integer id) {
        try {
            String mensaje = categoriaService.eliminar(id);
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
