package com.example.msnotificacion.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.msnotificacion.DTO.NotificacionDTO;
import com.example.msnotificacion.model.Notificacion;
import com.example.msnotificacion.repository.NotificacionRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    public List<NotificacionDTO> obtenerTodas() {
        log.info("Consultando todas las notificaciones");
        return notificacionRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public NotificacionDTO buscarPorId(Integer id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificacion no encontrada"));
        return convertirADTO(notificacion);
    }

    public List<NotificacionDTO> buscarPorUsuario(Integer idUsuario) {
        return notificacionRepository.findByIdUsuario(idUsuario).stream()
                .map(this::convertirADTO)
                .toList();
    }

    public Notificacion guardar(Notificacion notificacion) {
        log.info("Guardando una nueva notificacion...");
        notificacion.setFechaEnvio(LocalDateTime.now());
        return notificacionRepository.save(notificacion);
    }

    public Notificacion marcarLeido(Integer id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificacion no encontrada"));
        notificacion.setLeido(true);
        return notificacionRepository.save(notificacion);
    }

    public String eliminar(Integer id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificacion no encontrada"));
        notificacionRepository.delete(notificacion);
        return "Notificacion eliminada correctamente";
    }

    private NotificacionDTO convertirADTO(Notificacion notificacion) {
        NotificacionDTO dto = new NotificacionDTO();
        dto.setId(notificacion.getId());
        dto.setIdUsuario(notificacion.getIdUsuario());
        dto.setTipo(notificacion.getTipo());
        dto.setMensaje(notificacion.getMensaje());
        dto.setLeido(notificacion.isLeido());
        dto.setFechaEnvio(notificacion.getFechaEnvio());
        return dto;
    }
}

