package com.example.msproducto.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.msproducto.DTO.InventarioDTO;
import com.example.msproducto.model.Inventario;
import com.example.msproducto.repository.InventarioRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    public List<InventarioDTO> obtenerTodos() {
        return inventarioRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public InventarioDTO buscarPorId(Integer id) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));

        return convertirADTO(inventario);
    }

    public Inventario guardar(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    public Inventario actualizarInventario(Integer id, Inventario inventarioActualizado) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));

        if (inventarioActualizado.getStock() != null) {
            inventario.setStock(inventarioActualizado.getStock());
        }

        if (inventarioActualizado.getStockMinimo() != null) {
            inventario.setStockMinimo(inventarioActualizado.getStockMinimo());
        }

        if (inventarioActualizado.getUbicacion() != null) {
            inventario.setUbicacion(inventarioActualizado.getUbicacion());
        }

        return inventarioRepository.save(inventario);
    }

    public String eliminar(Integer id) {
        try {
            Inventario inventario = inventarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));

            inventarioRepository.delete(inventario);

            return "Inventario eliminado correctamente";

        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    private InventarioDTO convertirADTO(Inventario inventario) {

        InventarioDTO dto = new InventarioDTO();

        dto.setId(inventario.getId());
        dto.setStock(inventario.getStock());
        dto.setStockMinimo(inventario.getStockMinimo());
        dto.setUbicacion(inventario.getUbicacion());

        if (inventario.getProducto() != null) {
            dto.setNombreProducto(inventario.getProducto().getNombre());
        } else {
            dto.setNombreProducto("Sin producto");
        }

        return dto;
    }
}
