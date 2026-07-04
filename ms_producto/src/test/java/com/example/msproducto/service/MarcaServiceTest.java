package com.example.msproducto.service;

import com.example.msproducto.DTO.MarcaDTO;
import com.example.msproducto.model.Marca;
import com.example.msproducto.model.Producto;
import com.example.msproducto.repository.MarcaRepository;
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
class MarcaServiceTest {

    @Mock
    private MarcaRepository marcaRepository;

    @InjectMocks
    private MarcaService marcaService;

    private Marca marcaEjemplo;

    @BeforeEach
    void setUp() {
        Producto producto = new Producto();
        producto.setId(1);
        producto.setNombre("Ryzen 5 5600X");

        marcaEjemplo = new Marca();
        marcaEjemplo.setId(1);
        marcaEjemplo.setNombre("AMD");
        marcaEjemplo.setProductos(List.of(producto));
    }

    @Test
    @DisplayName("obtenerTodas() debe retornar lista de MarcaDTO con nombres de productos")
    void obtenerTodas_conMarcasExistentes_retornaListaDTO() {
        when(marcaRepository.findAll()).thenReturn(List.of(marcaEjemplo));

        List<MarcaDTO> resultado = marcaService.obtenerTodas();

        assertEquals(1, resultado.size());
        assertEquals("AMD", resultado.get(0).getNombre());
        assertTrue(resultado.get(0).getNombresProductos().contains("Ryzen 5 5600X"));
    }

    @Test
    @DisplayName("buscarPorId() debe lanzar RuntimeException cuando la marca no existe")
    void buscarPorId_conIdInexistente_lanzaException() {
        when(marcaRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> marcaService.buscarPorId(99));
        assertEquals("Marca no encontrada", ex.getMessage());
    }

    @Test
    @DisplayName("eliminar() debe retornar mensaje de exito al eliminar una marca existente")
    void eliminar_conIdExistente_retornaMensajeExito() {
        when(marcaRepository.findById(1)).thenReturn(Optional.of(marcaEjemplo));
        doNothing().when(marcaRepository).delete(marcaEjemplo);

        String resultado = marcaService.eliminar(1);

        assertEquals("Marca eliminada correctamente", resultado);
        verify(marcaRepository, times(1)).delete(marcaEjemplo);
    }

    @Test
    @DisplayName("eliminar() debe lanzar RuntimeException cuando la marca no existe")
    void eliminar_conIdInexistente_lanzaException() {
        when(marcaRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> marcaService.eliminar(99));
        verify(marcaRepository, never()).delete(any());
    }
}
