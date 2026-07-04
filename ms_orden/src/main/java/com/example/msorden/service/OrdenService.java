package com.example.msorden.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.msorden.DTO.OrdenDTO;
import com.example.msorden.model.Orden;
import com.example.msorden.repository.OrdenRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrdenService {

    @Autowired
    private OrdenRepository ordenRepository;

    public List<OrdenDTO> obtenerTodas() {
        log.info("Buscando todas las ordenes en la base de datos");
        return ordenRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public OrdenDTO buscarPorId(Integer id) {
        log.info("Buscando orden por ID: {}", id);
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se encontro la orden por el ID: {}", id);
                    return new RuntimeException("Orden no encontrada");
                });

        return convertirADTO(orden);
    }

    public Orden actualizar(Integer id, Orden ordenActualizada) {
        log.info("Iniciando actualizacion de la orden por ID: {}", id);
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se puede actualizar. Orden ID {} no existen", id);
                    return new RuntimeException("Orden no encontrada");
                });
        if (ordenActualizada.getTotal() != null) {
            orden.setTotal(ordenActualizada.getTotal());
        }

        log.info("Orden ID: {} actualizada exitosamente", id);
        return ordenRepository.save(orden);
    }

    public String eliminar(Integer id) {
        log.info("Intentando eliminar orden con ID: {}", id);
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se puede eliminar. Orden ID {} no existe", id);
                    return new RuntimeException("Orden no encontrada");
                });
        ordenRepository.delete(orden);
        log.info("Orden ID: {} eliminada correctamente", id);
        return "Orden eliminada correctamente";
    }

    public Orden guardar(Orden orden) {
        log.info("Guardando un nuevo orden...");
        return ordenRepository.save(orden);
    }

    private OrdenDTO convertirADTO(Orden orden) {

        OrdenDTO dto = new OrdenDTO();

        dto.setId(orden.getId());
        dto.setTotal(orden.getTotal());
        dto.setIdUsuario(orden.getIdUsuario());

        return dto;
    }
}
