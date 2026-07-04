package com.example.mspago.service;

import com.example.mspago.DTO.EnvioDTO;
import com.example.mspago.model.Envio;
import com.example.mspago.repository.EnvioRepository;
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
class EnvioServiceTest {

    @Mock
    private EnvioRepository envioRepository;

    @InjectMocks
    private EnvioService envioService;

    private Envio envioEjemplo;

    @BeforeEach
    void setUp() {
        envioEjemplo = new Envio();
        envioEjemplo.setId(1);
        envioEjemplo.setDireccion("Av. Siempre Viva 123");
        envioEjemplo.setEstado("En preparación");
        envioEjemplo.setCodigoSeguimiento("TS-000123");
        envioEjemplo.setIdOrden(1);
    }

    @Test
    @DisplayName("obtenerTodos() debe retornar lista de EnvioDTO")
    void obtenerTodos_conEnviosExistentes_retornaListaDTO() {
        when(envioRepository.findAll()).thenReturn(List.of(envioEjemplo));

        List<EnvioDTO> resultado = envioService.obtenerTodos();

        assertEquals(1, resultado.size());
        assertEquals("TS-000123", resultado.get(0).getCodigoSeguimiento());
        assertEquals(1, resultado.get(0).getIdOrden());
    }

    @Test
    @DisplayName("buscarPorId() debe lanzar RuntimeException cuando el envio no existe")
    void buscarPorId_conIdInexistente_lanzaException() {
        when(envioRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> envioService.buscarPorId(99));
        assertEquals("Envio no encontrado", ex.getMessage());
    }

    @Test
    @DisplayName("actualizar() debe modificar el estado del envio")
    void actualizar_nuevoEstado_actualizaCorrectamente() {
        Envio cambios = new Envio();
        cambios.setEstado("Despachado");

        when(envioRepository.findById(1)).thenReturn(Optional.of(envioEjemplo));
        when(envioRepository.save(any(Envio.class))).thenReturn(envioEjemplo);

        Envio resultado = envioService.actualizar(1, cambios);

        assertEquals("Despachado", resultado.getEstado());
        assertEquals("TS-000123", resultado.getCodigoSeguimiento(), "El codigo no debe cambiar");
    }

    @Test
    @DisplayName("eliminar() debe retornar mensaje de exito al eliminar un envio existente")
    void eliminar_conIdExistente_retornaMensajeExito() {
        when(envioRepository.findById(1)).thenReturn(Optional.of(envioEjemplo));
        doNothing().when(envioRepository).delete(envioEjemplo);

        String resultado = envioService.eliminar(1);

        assertEquals("Envio eliminado correctamente", resultado);
        verify(envioRepository, times(1)).delete(envioEjemplo);
    }

    @Test
    @DisplayName("eliminar() debe lanzar RuntimeException cuando el envio no existe")
    void eliminar_conIdInexistente_lanzaException() {
        when(envioRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> envioService.eliminar(99));
        verify(envioRepository, never()).delete(any());
    }
}
