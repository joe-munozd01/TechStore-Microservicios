package com.example.msproducto.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.msproducto.DTO.MarcaDTO;
import com.example.msproducto.model.Marca;
import com.example.msproducto.model.Producto;
import com.example.msproducto.repository.MarcaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MarcaService {

    @Autowired
    private MarcaRepository marcaRepository;

    public List<MarcaDTO> obtenerTodas() {
        log.info("Buscando todas las marcas en la base de datos");
        return marcaRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public MarcaDTO buscarPorId(Integer id) {
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se encontro la marca por ID: {}", id);
                    return new RuntimeException("Marca no encontrada");
                });

        return convertirADTO(marca);
    }

    public Marca actualizar(Integer id, Marca marcaActualizada) {
        log.info("Iniciando actualizacion de la marca con ID: {}", id);
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se puede actualizar. Marca ID {} no existe", id);
                    return new RuntimeException("Marca no encontrada");
                });

        if (marcaActualizada.getNombre() != null) {
            marca.setNombre(marcaActualizada.getNombre());
        }

        log.info("Marca ID: {} actualizada exitosamente", id);
        return marcaRepository.save(marca);
    }

    public String eliminar(Integer id) {
        log.info("Intentando eliminar marca con ID: {}", id);
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se puede eliminar. Marca ID {} no existe", id);
                    return new RuntimeException("Marca no encontrada");
                });
        marcaRepository.delete(marca);
        log.info("Marca ID: {} eliminada correctamente", id);
        return "Marca eliminada correctamente";
    }

    public Marca guardar(Marca marca) {
        log.info("Guardando una nueva marca...");
        return marcaRepository.save(marca);
    }

    private MarcaDTO convertirADTO(Marca marca) {

        MarcaDTO dto = new MarcaDTO();

        dto.setId(marca.getId());
        dto.setNombre(marca.getNombre());

        if (marca.getProductos() != null) {
            dto.setNombresProductos(
                    marca.getProductos().stream()
                            .map(Producto::getNombre)
                            .toList()
            );
        } else {
            dto.setNombresProductos(new ArrayList<>());
        }
        return dto;
    }
}
