package com.example.mssoporte.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.mssoporte.DTO.TicketDTO;
import com.example.mssoporte.model.Ticket;
import com.example.mssoporte.repository.TicketRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    public List<TicketDTO> obtenerTodos() {
        log.info("Consultando todos los tickets");
        return ticketRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public TicketDTO buscarPorId(Integer id) {
        log.info("Buscando ticket por el ID {}", id);
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se encontro el ticket por el ID {}", id);
                    return new RuntimeException("Ticket no encontrado");
                });
        return convertirADTO(ticket);
    }

    public List<TicketDTO> buscarPorUsuario(Integer idUsuario) {
        log.info("Buscando tickets del usuario {}", idUsuario);
        return ticketRepository.findByIdUsuario(idUsuario).stream()
                .map(this::convertirADTO)
                .toList();
    }

    public Ticket guardar(Ticket ticket) {
        log.info("Guardando un nuevo ticket...");
        ticket.setFechaCreacion(java.time.LocalDateTime.now());
        return ticketRepository.save(ticket);
    }

    public Ticket actualizar(Integer id, Ticket datosActualizados) {
        log.info("Actualizando ticket ID {}", id);
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));

        if (datosActualizados.getAsunto() != null) ticket.setAsunto(datosActualizados.getAsunto());
        if (datosActualizados.getDescripcion() != null) ticket.setDescripcion(datosActualizados.getDescripcion());
        if (datosActualizados.getPrioridad() != null) ticket.setPrioridad(datosActualizados.getPrioridad());

        return ticketRepository.save(ticket);
    }

    public Ticket cambiarEstado(Integer id, String nuevoEstado) {
        log.info("Cambiando estado del ticket {} a {}", id, nuevoEstado);
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));
        ticket.setEstado(nuevoEstado);
        return ticketRepository.save(ticket);
    }

    public String eliminar(Integer id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));
        ticketRepository.delete(ticket);
        return "Ticket eliminado correctamente";
    }

    private TicketDTO convertirADTO(Ticket ticket) {
        TicketDTO dto = new TicketDTO();
        dto.setId(ticket.getId());
        dto.setAsunto(ticket.getAsunto());
        dto.setDescripcion(ticket.getDescripcion());
        dto.setEstado(ticket.getEstado());
        dto.setPrioridad(ticket.getPrioridad());
        dto.setIdUsuario(ticket.getIdUsuario());
        dto.setFechaCreacion(ticket.getFechaCreacion());
        return dto;
    }
}
