package com.example.msorden.service;

import com.example.msorden.DTO.CarritoDTO;
import com.example.msorden.model.Carrito;
import com.example.msorden.model.CarritoDetalle;
import com.example.msorden.repository.CarritoRepository;
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
class CarritoServiceTest {

    @Mock
    private CarritoRepository carritoRepository;

    @InjectMocks
    private CarritoService carritoService;

    private Carrito carritoEjemplo;

    @BeforeEach
    void setUp() {
        CarritoDetalle detalle = new CarritoDetalle();
        detalle.setId(1);
        detalle.setCantidad(2);
        detalle.setIdProducto(10);

        carritoEjemplo = new Carrito();
        carritoEjemplo.setId(1);
        carritoEjemplo.setIdUsuario(5);
        carritoEjemplo.setDetalles(List.of(detalle));
    }

    @Test
    @DisplayName("obtenerTodos() debe retornar lista de CarritoDTO con ids de productos")
    void obtenerTodos_conCarritosExistentes_retornaListaDTO() {
        when(carritoRepository.findAll()).thenReturn(List.of(carritoEjemplo));

        List<CarritoDTO> resultado = carritoService.obtenerTodos();

        assertEquals(1, resultado.size());
        assertEquals(5, resultado.get(0).getIdUsuario());
        assertTrue(resultado.get(0).getIdsProductos().contains(10));
    }

    @Test
    @DisplayName("buscarPorId() debe lanzar RuntimeException cuando el carrito no existe")
    void buscarPorId_conIdInexistente_lanzaException() {
        when(carritoRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> carritoService.buscarPorId(99));
        assertEquals("Carrito no encontrado", ex.getMessage());
    }

    @Test
    @DisplayName("actualizar() debe modificar el idUsuario del carrito")
    void actualizar_nuevoIdUsuario_actualizaCorrectamente() {
        Carrito cambios = new Carrito();
        cambios.setIdUsuario(9);

        when(carritoRepository.findById(1)).thenReturn(Optional.of(carritoEjemplo));
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carritoEjemplo);

        Carrito resultado = carritoService.actualizar(1, cambios);

        assertEquals(9, resultado.getIdUsuario());
    }

    @Test
    @DisplayName("eliminar() debe retornar mensaje de exito para carrito existente")
    void eliminar_conIdExistente_retornaMensajeExito() {
        when(carritoRepository.findById(1)).thenReturn(Optional.of(carritoEjemplo));
        doNothing().when(carritoRepository).delete(carritoEjemplo);

        String resultado = carritoService.eliminar(1);

        assertEquals("Carrito eliminado correctamente", resultado);
        verify(carritoRepository, times(1)).delete(carritoEjemplo);
    }

    @Test
    @DisplayName("eliminar() debe lanzar RuntimeException cuando el carrito no existe")
    void eliminar_conIdInexistente_lanzaException() {
        when(carritoRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> carritoService.eliminar(99));
        verify(carritoRepository, never()).delete(any());
    }
}
