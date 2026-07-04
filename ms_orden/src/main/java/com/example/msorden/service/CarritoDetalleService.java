package com.example.msorden.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.msorden.DTO.CarritoDetalleDTO;
import com.example.msorden.model.CarritoDetalle;
import com.example.msorden.repository.CarritoDetalleRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CarritoDetalleService {

    @Autowired
    private CarritoDetalleRepository carritoDetalleRepository;

    public List<CarritoDetalleDTO> obtenerTodos() {
        log.info("Consultando todos los detalles en la base de datos");
        return carritoDetalleRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public CarritoDetalleDTO buscarPorId(Integer id) {
        log.info("Buscando detalle por ID: {}", id);
        CarritoDetalle detalle = carritoDetalleRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se encontro el detalle por ID: {}", id);
                    return new RuntimeException("Detalle no encontrado");
                });

        return convertirADTO(detalle);
    }

    public CarritoDetalle actualizar(Integer id, CarritoDetalle detalleActualizado) {
        log.info("Iniciando actualización del detalle de carrito ID: {}", id);
        CarritoDetalle detalle = carritoDetalleRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se puede actualizar. El detalle ID {} no existe", id);
                    return new RuntimeException("Detalle no encontrado");
                });

        if (detalleActualizado.getCantidad() != null) {
            detalle.setCantidad(detalleActualizado.getCantidad());
        }

        log.info("Detalle de carrito ID: {} actualizado exitosamente", id);
        return carritoDetalleRepository.save(detalle);
    }

    public String eliminar(Integer id) {
        log.info("Intentando eliminar el detalle de carrito ID: {}", id);
        CarritoDetalle detalle = carritoDetalleRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se puede eliminar. El detalle ID {} no existe", id);
                    return new RuntimeException("Detalle no encontrado");
                });

        carritoDetalleRepository.delete(detalle);
        log.info("Detalle de carrito ID: {} eliminado correctamente", id);
        return "Detalle eliminado correctamente";
    }

    public CarritoDetalle guardar(CarritoDetalle detalle) {
        log.info("Guardando un nuevo detalle...");
        return carritoDetalleRepository.save(detalle);
    }

    private CarritoDetalleDTO convertirADTO(CarritoDetalle detalle) {

        CarritoDetalleDTO dto = new CarritoDetalleDTO();

        dto.setId(detalle.getId());
        dto.setCantidad(detalle.getCantidad());
        dto.setIdProducto(detalle.getIdProducto());

        return dto;
    }
}
