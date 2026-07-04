package com.example.msproducto.service;

import com.example.msproducto.DTO.InventarioDTO;
import com.example.msproducto.model.Inventario;
import com.example.msproducto.model.Producto;
import com.example.msproducto.repository.InventarioRepository;
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
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioService inventarioService;

    private Inventario inventarioEjemplo;
    private Producto productoEjemplo;

    @BeforeEach
    void setUp() {
        productoEjemplo = new Producto();
        productoEjemplo.setId(1);
        productoEjemplo.setNombre("Ryzen 5 5600X");
        productoEjemplo.setDescripcion("Procesador 6 nucleos");
        productoEjemplo.setPrecio(120000);

        inventarioEjemplo = new Inventario();
        inventarioEjemplo.setId(1);
        inventarioEjemplo.setStock(50);
        inventarioEjemplo.setStockMinimo(5);
        inventarioEjemplo.setUbicacion("Bodega A - Estante 3");
        inventarioEjemplo.setProducto(productoEjemplo);
    }

    @Test
    @DisplayName("obtenerTodos() debe retornar lista de InventarioDTO con nombre de producto")
    void obtenerTodos_conInventariosExistentes_retornaListaDTO() {

        when(inventarioRepository.findAll()).thenReturn(List.of(inventarioEjemplo));

        List<InventarioDTO> resultado = inventarioService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(50, resultado.get(0).getStock());
        assertEquals("Bodega A - Estante 3", resultado.get(0).getUbicacion());
        assertEquals("Ryzen 5 5600X", resultado.get(0).getNombreProducto());
    }

    @Test
    @DisplayName("buscarPorId() debe retornar InventarioDTO cuando el ID existe")
    void buscarPorId_conIdExistente_retornaDTO() {

        when(inventarioRepository.findById(1)).thenReturn(Optional.of(inventarioEjemplo));

        InventarioDTO resultado = inventarioService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals(50, resultado.getStock());
        assertEquals(5, resultado.getStockMinimo());
    }

    @Test
    @DisplayName("buscarPorId() debe lanzar RuntimeException cuando el ID no existe")
    void buscarPorId_conIdInexistente_lanzaException() {
        when(inventarioRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> inventarioService.buscarPorId(99));
        assertEquals("Inventario no encontrado", ex.getMessage());
    }

    @Test
    @DisplayName("actualizarInventario() debe actualizar solo los campos proporcionados")
    void actualizarInventario_soloStock_actualizaCorrectamente() {

        Inventario cambios = new Inventario();
        cambios.setStock(100);

        when(inventarioRepository.findById(1)).thenReturn(Optional.of(inventarioEjemplo));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventarioEjemplo);

        Inventario resultado = inventarioService.actualizarInventario(1, cambios);

        assertNotNull(resultado);
        assertEquals(100, resultado.getStock());
        assertEquals(5, resultado.getStockMinimo(), "El stock minimo no debe cambiar");
        assertEquals("Bodega A - Estante 3", resultado.getUbicacion(), "La ubicacion no debe cambiar");
    }

    @Test
    @DisplayName("eliminar() debe retornar mensaje de exito al eliminar un inventario existente")
    void eliminar_conIdExistente_retornaMensajeExito() {

        when(inventarioRepository.findById(1)).thenReturn(Optional.of(inventarioEjemplo));
        doNothing().when(inventarioRepository).delete(inventarioEjemplo);

        String resultado = inventarioService.eliminar(1);

        assertEquals("Inventario eliminado correctamente", resultado);
        verify(inventarioRepository, times(1)).delete(inventarioEjemplo);
    }

    @Test
    @DisplayName("eliminar() debe retornar mensaje de error cuando el inventario no existe")
    void eliminar_conIdInexistente_retornaMensajeError() {

        when(inventarioRepository.findById(99)).thenReturn(Optional.empty());

        String resultado = inventarioService.eliminar(99);

        assertEquals("Inventario no encontrado", resultado);
        verify(inventarioRepository, never()).delete(any());
    }

    @Test
    @DisplayName("DTO debe mostrar 'Sin producto' cuando el inventario no tiene producto asociado")
    void obtenerTodos_inventarioSinProducto_usaValorPorDefecto() {

        Inventario sinProducto = new Inventario();
        sinProducto.setId(2);
        sinProducto.setStock(10);
        sinProducto.setStockMinimo(2);
        sinProducto.setUbicacion("Zona retiro");
        sinProducto.setProducto(null);

        when(inventarioRepository.findAll()).thenReturn(List.of(sinProducto));

        List<InventarioDTO> resultado = inventarioService.obtenerTodos();

        assertEquals("Sin producto", resultado.get(0).getNombreProducto());
    }
}
