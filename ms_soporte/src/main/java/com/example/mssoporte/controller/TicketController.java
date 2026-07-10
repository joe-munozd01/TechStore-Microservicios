package com.example.mssoporte.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.mssoporte.DTO.TicketDTO;
import com.example.mssoporte.model.Ticket;
import com.example.mssoporte.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/tickets")
@Tag(name = "Tickets", description = "GestiÃ³n de tickets de soporte de TechStore")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Operation(summary = "Listar todos los tickets")
    @GetMapping
    public ResponseEntity<?> listarTickets() {
        List<TicketDTO> tickets = ticketService.obtenerTodos();
        if (tickets.isEmpty()) {
            return new ResponseEntity<>("No hay tickets", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @Operation(summary = "Buscar ticket por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarTicket(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(ticketService.buscarPorId(id), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Listar tickets de un usuario")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> buscarPorUsuario(@PathVariable Integer idUsuario) {
        return new ResponseEntity<>(ticketService.buscarPorUsuario(idUsuario), HttpStatus.OK);
    }

    @Operation(summary = "Crear nuevo ticket")
    @PostMapping
    public ResponseEntity<?> crearTicket(@Valid @RequestBody Ticket ticket) {
        try {
            return new ResponseEntity<>(ticketService.guardar(ticket), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("No se pudo crear el ticket", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar ticket existente")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarTicket(@PathVariable Integer id, @RequestBody Ticket ticket) {
        try {
            return new ResponseEntity<>(ticketService.actualizar(id, ticket), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Cambiar estado del ticket")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Integer id, @RequestParam String estado) {
        try {
            return new ResponseEntity<>(ticketService.cambiarEstado(id, estado), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar ticket por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarTicket(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(ticketService.eliminar(id), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
