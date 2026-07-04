package com.example.msproducto.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.msproducto.DTO.CategoriaDTO;
import com.example.msproducto.model.Categoria;
import com.example.msproducto.model.Producto;
import com.example.msproducto.repository.CategoriaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<CategoriaDTO> obtenerTodas() {
        log.info("Consultando todas las categorias en la base de datos");
        return categoriaRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public CategoriaDTO buscarPorId(Integer id) {
        log.info("Buscando categoria por ID: {}", id);
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se encontro la categoria por ID: {}", id);
                    return new RuntimeException("Categoria no encontrada");
                });
        return convertirADTO(categoria);
    }

    public Categoria actualizar(Integer id, Categoria categoriaActualizada) {
        log.info("Iniciando actualizacion de la categoria con ID: {}", id);
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se puede actualizar. Categoria ID {} no existe", id);
                    return new RuntimeException("Categoria no encontrada");
                });

        if (categoriaActualizada.getNombre() != null) {
            categoria.setNombre(categoriaActualizada.getNombre());
        }

        log.info("Categoria ID: {} actualizada exitosamente", id);
        return categoriaRepository.save(categoria);
    }

    public String eliminar(Integer id) {
        log.info("Intentando eliminar categoria con ID: {}", id);
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se puede eliminar. Categoria ID {} no existe", id);
                    return new RuntimeException("Categoria no encontrada");
                });
        categoriaRepository.delete(categoria);
        log.info("Categoria ID: {} eliminada correctamente", id);
        return "Categoria eliminada correctamente";
    }

    public Categoria guardar(Categoria categoria) {
        log.info("Guardando una nueva categoria...");
        return categoriaRepository.save(categoria);
    }

    private CategoriaDTO convertirADTO(Categoria categoria) {

        CategoriaDTO dto = new CategoriaDTO();

        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());

        if (categoria.getProductos() != null) {
            dto.setNombresProductos(
                    categoria.getProductos().stream()
                            .map(Producto::getNombre)
                            .toList()
            );
        } else {
            dto.setNombresProductos(new ArrayList<>());
        }

        return dto;
    }
}
