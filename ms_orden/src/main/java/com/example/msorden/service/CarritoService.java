package com.example.msorden.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.msorden.DTO.CarritoDTO;
import com.example.msorden.model.Carrito;
import com.example.msorden.model.CarritoDetalle;
import com.example.msorden.repository.CarritoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    public List<CarritoDTO> obtenerTodos() {
        log.info("Consultando todos los carritos en la base de datos");
        return carritoRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public CarritoDTO buscarPorId(Integer id) {
        log.info("Buscando carrito por ID: {}", id);
        Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se encontro el carrito por ID: {}", id);
                    return new RuntimeException("Carrito no encontrado");
                });

        return convertirADTO(carrito);
    }

    public Carrito actualizar(Integer id, Carrito carritoActualizado) {
        log.info("Iniciando actualizacion del carrito por ID: {}", id);
        Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se puede actualizar. Carrito ID {} no existe", id);
                    return new RuntimeException("Carrito no encontrado");
                });
        if (carritoActualizado.getIdUsuario() != null) {
            carrito.setIdUsuario(carritoActualizado.getIdUsuario());
        }

        log.info("Carrito ID: {} actualizado exitosamente", id);
        return carritoRepository.save(carrito);
    }

    public String eliminar(Integer id) {
        log.info("Intentado eliminar carrito con ID: {}", id);
        Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se puede eliminar. Carrito ID {} no existe", id);
                    return new RuntimeException("Carrito no encontrado");
                });

        carritoRepository.delete(carrito);
        log.info("Carrito ID: {} eliminado correctamente", id);
        return "Carrito eliminado correctamente";
    }

    public Carrito guardar(Carrito carrito) {
        log.info("Guardando un nuevo carrito...");
        return carritoRepository.save(carrito);
    }

    private CarritoDTO convertirADTO(Carrito carrito) {

        CarritoDTO dto = new CarritoDTO();

        dto.setId(carrito.getId());
        dto.setIdUsuario(carrito.getIdUsuario());

        if (carrito.getDetalles() != null) {
            dto.setIdsProductos(
                    carrito.getDetalles().stream()
                            .map(CarritoDetalle::getIdProducto)
                            .toList()
            );
        } else {
            dto.setIdsProductos(new ArrayList<>());
        }
        return dto;
    }
}
