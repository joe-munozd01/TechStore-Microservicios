package com.example.msorden.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.msorden.DTO.DetalleOrdenDTO;
import com.example.msorden.model.DetalleOrden;
import com.example.msorden.repository.DetalleOrdenRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DetalleOrdenService {

    @Autowired
    private DetalleOrdenRepository detalleOrdenRepository;

    public List<DetalleOrdenDTO> obtenerTodos() {
        log.info("Consultando detalles en la base de datos");
        return detalleOrdenRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public DetalleOrdenDTO buscarPorId(Integer id) {
        log.info("Buscando por detalles por ID: {}", id);
        DetalleOrden detalle = detalleOrdenRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se encontro el detalle con ID: {}", id);
                    return new RuntimeException("Detalle no encontrado");
                });

        return convertirADTO(detalle);
    }

    public DetalleOrden actualizar(Integer id, DetalleOrden detalleActualizado) {
        log.info("Iniciando actualizacion del detalle de orden con ID: {}", id);
        DetalleOrden detalle = detalleOrdenRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se puede actualizar. El detalle de orden ID {} no existe", id);
                    return new RuntimeException("Detalle de orden no encontrado");
                });

        if (detalleActualizado.getCantidad() != null) {
            detalle.setCantidad(detalleActualizado.getCantidad());
        }
        if (detalleActualizado.getSubtotal() != null) {
            detalle.setSubtotal(detalleActualizado.getSubtotal());
        }

        log.info("Detalle de orden ID: {} actualizado exitosamente", id);
        return detalleOrdenRepository.save(detalle);
    }

    public String eliminar(Integer id) {
        log.info("Intentando eliminar detalle de orden con ID: {}", id);
        DetalleOrden detalle = detalleOrdenRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se puede eliminar. El detalle de orden ID {} no existe", id);
                    return new RuntimeException("Detalle de orden no encontrado");
                });

        detalleOrdenRepository.delete(detalle);
        log.info("Detalle de orden ID: {} eliminado correctamente", id);
        return "Detalle de orden eliminado correctamente";
    }

    public DetalleOrden guardar(DetalleOrden detalle) {
        log.info("Guardando un nuevo detalle...");
        return detalleOrdenRepository.save(detalle);
    }

    private DetalleOrdenDTO convertirADTO(DetalleOrden detalle) {

        DetalleOrdenDTO dto = new DetalleOrdenDTO();

        dto.setId(detalle.getId());
        dto.setCantidad(detalle.getCantidad());
        dto.setSubtotal(detalle.getSubtotal());
        dto.setIdProducto(detalle.getIdProducto());

        return dto;
    }
}
