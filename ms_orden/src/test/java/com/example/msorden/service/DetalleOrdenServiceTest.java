package com.example.msorden.service;

import com.example.msorden.DTO.DetalleOrdenDTO;
import com.example.msorden.model.DetalleOrden;
import com.example.msorden.repository.DetalleOrdenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DetalleOrdenServiceTest {

    @Mock
    private DetalleOrdenRepository detalleOrdenRepository;

    @InjectMocks
    private DetalleOrdenService detalleOrdenService;

    private DetalleOrden detalleEjemplo;

    @BeforeEach
    void setUp() {
        detalleEjemplo = new DetalleOrden();
        detalleEjemplo.setId(1);
        detalleEjemplo.setCantidad(2);
        detalleEjemplo.setSubtotal(240000);
        detalleEjemplo.setIdProducto(10);
    }

    @Test
    @DisplayName("obtenerTodos() debe retornar lista de DetalleOrdenDTO")
    void obtenerTodos_conDetallesExistentes_retornaListaDTO() {
        when(detalleOrdenRepository.findAll()).thenReturn(List.of(detalleEjemplo));

        List<DetalleOrdenDTO> resultado = detalleOrdenService.obtenerTodos();

        assertEquals(1, resultado.size());
        assertEquals(240000, resultado.get(0).getSubtotal());
        assertEquals(10, resultado.get(0).getIdProducto());
    }

    @Test
    @DisplayName("buscarPorId() debe lanzar RuntimeException cuando el detalle no existe")
    void buscarPorId_conIdInexistente_lanzaException() {
        when(detalleOrdenRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> detalleOrdenService.buscarPorId(99));
        assertEquals("Detalle no encontrado", ex.getMessage());
    }

    @Test
    @DisplayName("actualizar() debe modificar cantidad y subtotal")
    void actualizar_nuevaCantidadYSubtotal_actualizaCorrectamente() {
        DetalleOrden cambios = new DetalleOrden();
        cambios.setCantidad(5);
        cambios.setSubtotal(600000);

        when(detalleOrdenRepository.findById(1)).thenReturn(Optional.of(detalleEjemplo));
        when(detalleOrdenRepository.save(any(DetalleOrden.class))).thenReturn(detalleEjemplo);

        DetalleOrden resultado = detalleOrdenService.actualizar(1, cambios);

        assertEquals(5, resultado.getCantidad());
        assertEquals(600000, resultado.getSubtotal());
    }

    @Test
    @DisplayName("eliminar() debe retornar mensaje de exito para detalle existente")
    void eliminar_conIdExistente_retornaMensajeExito() {
        when(detalleOrdenRepository.findById(1)).thenReturn(Optional.of(detalleEjemplo));
        doNothing().when(detalleOrdenRepository).delete(detalleEjemplo);

        String resultado = detalleOrdenService.eliminar(1);

        assertEquals("Detalle de orden eliminado correctamente", resultado);
        verify(detalleOrdenRepository, times(1)).delete(detalleEjemplo);
    }

    @Test
    @DisplayName("eliminar() debe lanzar RuntimeException cuando el detalle no existe")
    void eliminar_conIdInexistente_lanzaException() {
        when(detalleOrdenRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> detalleOrdenService.eliminar(99));
        verify(detalleOrdenRepository, never()).delete(any());
    }
}
