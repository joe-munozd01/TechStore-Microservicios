package com.example.mspago.service;

import com.example.mspago.DTO.PagoDTO;
import com.example.mspago.model.Pago;
import com.example.mspago.repository.PagoRepository;
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
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private PagoService pagoService;

    private Pago pagoEjemplo;

    @BeforeEach
    void setUp() {
        pagoEjemplo = new Pago();
        pagoEjemplo.setId(1);
        pagoEjemplo.setMetodoPago("Transferencia bancaria");
        pagoEjemplo.setMonto(120000);
        pagoEjemplo.setIdOrden(1);
    }

    @Test
    @DisplayName("obtenerTodos() debe retornar lista de PagoDTO correctamente mapeada")
    void obtenerTodos_conPagosExistentes_retornaListaDTO() {

        when(pagoRepository.findAll()).thenReturn(List.of(pagoEjemplo));

        List<PagoDTO> resultado = pagoService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Transferencia bancaria", resultado.get(0).getMetodoPago());
        assertEquals(120000, resultado.get(0).getMonto());
        assertEquals(1, resultado.get(0).getIdOrden());
    }

    @Test
    @DisplayName("buscarPorId() debe retornar PagoDTO cuando el ID existe")
    void buscarPorId_conIdExistente_retornaDTO() {

        when(pagoRepository.findById(1)).thenReturn(Optional.of(pagoEjemplo));

        PagoDTO resultado = pagoService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals(120000, resultado.getMonto());
    }

    @Test
    @DisplayName("buscarPorId() debe lanzar RuntimeException cuando el pago no existe")
    void buscarPorId_conIdInexistente_lanzaException() {
        when(pagoRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> pagoService.buscarPorId(99));
        assertEquals("Pago no encontrado", ex.getMessage());
    }

    @Test
    @DisplayName("actualizar() debe actualizar el método de pago sin modificar el monto")
    void actualizar_soloMetodoPago_actualizaCorrectamente() {

        Pago cambios = new Pago();
        cambios.setMetodoPago("Tarjeta de crédito");

        when(pagoRepository.findById(1)).thenReturn(Optional.of(pagoEjemplo));
        when(pagoRepository.save(any(Pago.class))).thenReturn(pagoEjemplo);

        Pago resultado = pagoService.actualizar(1, cambios);

        assertEquals("Tarjeta de crédito", resultado.getMetodoPago());
        assertEquals(120000, resultado.getMonto(), "El monto no debe cambiar");
    }

    @Test
    @DisplayName("eliminar() debe retornar mensaje de éxito al eliminar un pago existente")
    void eliminar_conIdExistente_retornaMensajeExito() {
        when(pagoRepository.findById(1)).thenReturn(Optional.of(pagoEjemplo));
        doNothing().when(pagoRepository).delete(pagoEjemplo);

        String resultado = pagoService.eliminar(1);

        assertEquals("Pago eliminado correctamente", resultado);
        verify(pagoRepository, times(1)).delete(pagoEjemplo);
    }

    @Test
    @DisplayName("eliminar() debe lanzar RuntimeException cuando el pago no existe")
    void eliminar_conIdInexistente_lanzaException() {
        when(pagoRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> pagoService.eliminar(99));
        assertEquals("Pago no encontrado", ex.getMessage());
        verify(pagoRepository, never()).delete(any());
    }

    @Test
    @DisplayName("guardar() debe persistir el pago aunque la API de mindicador.cl no responda")
    void guardar_cuandoApiExternaFalla_elPagoSeGuardaIgualmente() {
        Pago nuevoPago = new Pago();
        nuevoPago.setMetodoPago("WebPay");
        nuevoPago.setMonto(89990);
        nuevoPago.setIdOrden(3);

        when(pagoRepository.save(any(Pago.class))).thenAnswer(inv -> {
            Pago p = inv.getArgument(0);
            p.setId(5);
            return p;
        });

        Pago resultado = pagoService.guardar(nuevoPago);

        assertNotNull(resultado, "El pago no debe ser nulo aunque la API externa falle");
        assertEquals(5, resultado.getId(), "El pago debe quedar persistido con ID asignado");
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }
}
