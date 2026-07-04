package com.example.msorden.service;

import com.example.msorden.DTO.OrdenDTO;
import com.example.msorden.model.Orden;
import com.example.msorden.repository.OrdenRepository;
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
class OrdenServiceTest {

    @Mock
    private OrdenRepository ordenRepository;

    @InjectMocks
    private OrdenService ordenService;

    private Orden ordenEjemplo;

    @BeforeEach
    void setUp() {
        ordenEjemplo = new Orden();
        ordenEjemplo.setId(1);
        ordenEjemplo.setTotal(355000);
        ordenEjemplo.setIdUsuario(1);
    }

    @Test
    @DisplayName("obtenerTodas() debe retornar lista de OrdenDTO")
    void obtenerTodas_conOrdenesExistentes_retornaListaDTO() {

        when(ordenRepository.findAll()).thenReturn(List.of(ordenEjemplo));

        List<OrdenDTO> resultado = ordenService.obtenerTodas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(355000, resultado.get(0).getTotal());
        assertEquals(1, resultado.get(0).getIdUsuario());
    }

    @Test
    @DisplayName("buscarPorId() debe retornar OrdenDTO cuando el ID existe")
    void buscarPorId_conIdExistente_retornaDTO() {

        when(ordenRepository.findById(1)).thenReturn(Optional.of(ordenEjemplo));

        OrdenDTO resultado = ordenService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals(355000, resultado.getTotal());
    }

    @Test
    @DisplayName("buscarPorId() debe lanzar RuntimeException cuando la orden no existe")
    void buscarPorId_conIdInexistente_lanzaException() {

        when(ordenRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> ordenService.buscarPorId(99));
        assertEquals("Orden no encontrada", ex.getMessage());
    }

    @Test
    @DisplayName("actualizar() debe modificar el total cuando se provee un nuevo valor")
    void actualizar_conNuevoTotal_actualizaCorrectamente() {

        Orden cambios = new Orden();
        cambios.setTotal(500000);

        when(ordenRepository.findById(1)).thenReturn(Optional.of(ordenEjemplo));
        when(ordenRepository.save(any(Orden.class))).thenReturn(ordenEjemplo);

        Orden resultado = ordenService.actualizar(1, cambios);

        assertEquals(500000, resultado.getTotal());
        verify(ordenRepository, times(1)).save(ordenEjemplo);
    }

    @Test
    @DisplayName("actualizar() debe lanzar RuntimeException cuando la orden no existe")
    void actualizar_conIdInexistente_lanzaException() {

        when(ordenRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> ordenService.actualizar(99, new Orden()));
        assertEquals("Orden no encontrada", ex.getMessage());
        verify(ordenRepository, never()).save(any());
    }

    @Test
    @DisplayName("eliminar() debe retornar mensaje de exito para orden existente")
    void eliminar_conIdExistente_retornaMensajeExito() {

        when(ordenRepository.findById(1)).thenReturn(Optional.of(ordenEjemplo));
        doNothing().when(ordenRepository).delete(ordenEjemplo);

        String resultado = ordenService.eliminar(1);

        assertEquals("Orden eliminada correctamente", resultado);
        verify(ordenRepository, times(1)).delete(ordenEjemplo);
    }

    @Test
    @DisplayName("eliminar() debe lanzar RuntimeException cuando la orden no existe")
    void eliminar_conIdInexistente_lanzaException() {

        when(ordenRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ordenService.eliminar(99));
        verify(ordenRepository, never()).delete(any());
    }
}
