package com.example.msdespacho.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.msdespacho.DTO.DespachoDTO;
import com.example.msdespacho.model.Despacho;
import com.example.msdespacho.repository.DespachoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DespachoService {

    @Autowired
    private DespachoRepository despachoRepository;

    public List<DespachoDTO> obtenerTodos() {
        log.info("Consultando todos los despachos");
        return despachoRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public DespachoDTO buscarPorId(Integer id) {
        log.info("Buscando despacho por el ID {}", id);
        Despacho despacho = despachoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Despacho no encontrado"));
        return convertirADTO(despacho);
    }

    public List<DespachoDTO> buscarPorOrden(Integer idOrden) {
        return despachoRepository.findByIdOrden(idOrden).stream()
                .map(this::convertirADTO)
                .toList();
    }

    public Despacho guardar(Despacho despacho) {
        log.info("Guardando un nuevo despacho...");
        return despachoRepository.save(despacho);
    }

    public Despacho actualizar(Integer id, Despacho datosActualizados) {
        Despacho despacho = despachoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Despacho no encontrado"));

        if (datosActualizados.getDireccion() != null) despacho.setDireccion(datosActualizados.getDireccion());
        if (datosActualizados.getTransportista() != null) despacho.setTransportista(datosActualizados.getTransportista());

        return despachoRepository.save(despacho);
    }

    public Despacho cambiarEstado(Integer id, String nuevoEstado) {
        Despacho despacho = despachoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Despacho no encontrado"));
        despacho.setEstado(nuevoEstado);
        if ("ENTREGADO".equalsIgnoreCase(nuevoEstado)) {
            despacho.setFechaDespacho(LocalDateTime.now());
        }
        return despachoRepository.save(despacho);
    }

    public String eliminar(Integer id) {
        Despacho despacho = despachoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Despacho no encontrado"));
        despachoRepository.delete(despacho);
        return "Despacho eliminado correctamente";
    }

    private DespachoDTO convertirADTO(Despacho despacho) {
        DespachoDTO dto = new DespachoDTO();
        dto.setId(despacho.getId());
        dto.setDireccion(despacho.getDireccion());
        dto.setEstado(despacho.getEstado());
        dto.setTransportista(despacho.getTransportista());
        dto.setIdOrden(despacho.getIdOrden());
        dto.setFechaDespacho(despacho.getFechaDespacho());
        return dto;
    }
}
