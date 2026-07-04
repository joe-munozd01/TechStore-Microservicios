package com.example.mspago.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.mspago.DTO.EnvioDTO;
import com.example.mspago.model.Envio;
import com.example.mspago.repository.EnvioRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EnvioService {

    @Autowired
    private EnvioRepository envioRepository;

    public List<EnvioDTO> obtenerTodos() {
        log.info("Buscando todos los envios en la base de datos...");
        return envioRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public EnvioDTO buscarPorId(Integer id) {
        log.info("Buscando envio con ID: {}", id);
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se encontro el envio con ID: {}", id);
                    return new RuntimeException("Envio no encontrado");
                });
        return convertirADTO(envio);
    }

    public Envio actualizar(Integer id, Envio envioActualizado) {
        log.info("Iniciando actualizacion del envio con ID: {}", id);
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se puede actualizar. Envio ID {} no existe", id);
                    return new RuntimeException("Envio no encontrado");
                });

        if (envioActualizado.getDireccion() != null) {
            envio.setDireccion(envioActualizado.getDireccion());
        }
        if (envioActualizado.getEstado() != null) {
            envio.setEstado(envioActualizado.getEstado());
        }
        if (envioActualizado.getCodigoSeguimiento() != null) {
            envio.setCodigoSeguimiento(envioActualizado.getCodigoSeguimiento());
        }
        if (envioActualizado.getIdOrden() != null) {
            envio.setIdOrden(envioActualizado.getIdOrden());
        }

        log.info("Envio ID: {} actualizado exitosamente", id);
        return envioRepository.save(envio);
    }

    public String eliminar(Integer id) {
        log.info("Intentando eliminar envio con ID: {}", id);
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se puede eliminar. Envio ID {} no existe", id);
                    return new RuntimeException("Envio no encontrado");
                });
        envioRepository.delete(envio);
        log.info("Envio ID: {} eliminado correctamente", id);
        return "Envio eliminado correctamente";
    }

    public Envio guardar(Envio envio) {
        log.info("Guardando nuevo envio con código de seguimiento: {}", envio.getCodigoSeguimiento());
        return envioRepository.save(envio);
    }

    private EnvioDTO convertirADTO(Envio envio) {
        EnvioDTO dto = new EnvioDTO();
        dto.setId(envio.getId());
        dto.setDireccion(envio.getDireccion());
        dto.setEstado(envio.getEstado());
        dto.setCodigoSeguimiento(envio.getCodigoSeguimiento());
        dto.setIdOrden(envio.getIdOrden());
        return dto;
    }
}
