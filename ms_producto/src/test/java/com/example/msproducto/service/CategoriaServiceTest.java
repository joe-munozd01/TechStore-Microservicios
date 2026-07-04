package com.example.msproducto.service;

import com.example.msproducto.DTO.CategoriaDTO;
import com.example.msproducto.model.Categoria;
import com.example.msproducto.model.Producto;
import com.example.msproducto.repository.CategoriaRepository;
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
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoriaEjemplo;

    @BeforeEach
    void setUp() {
        Producto producto = new Producto();
        producto.setId(1);
        producto.setNombre("Ryzen 5 5600X");

        categoriaEjemplo = new Categoria();
        categoriaEjemplo.setId(1);
        categoriaEjemplo.setNombre("Procesadores");
        categoriaEjemplo.setProductos(List.of(producto));
    }

    @Test
    @DisplayName("obtenerTodas() debe retornar lista de CategoriaDTO con nombres de productos")
    void obtenerTodas_conCategoriasExistentes_retornaListaDTO() {
        when(categoriaRepository.findAll()).thenReturn(List.of(categoriaEjemplo));

        List<CategoriaDTO> resultado = categoriaService.obtenerTodas();

        assertEquals(1, resultado.size());
        assertEquals("Procesadores", resultado.get(0).getNombre());
        assertTrue(resultado.get(0).getNombresProductos().contains("Ryzen 5 5600X"));
    }

    @Test
    @DisplayName("buscarPorId() debe lanzar RuntimeException cuando la categoria no existe")
    void buscarPorId_conIdInexistente_lanzaException() {
        when(categoriaRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> categoriaService.buscarPorId(99));
        assertEquals("Categoria no encontrada", ex.getMessage());
    }

    @Test
    @DisplayName("actualizar() debe modificar el nombre de la categoria")
    void actualizar_nuevoNombre_actualizaCorrectamente() {
        Categoria cambios = new Categoria();
        cambios.setNombre("Procesadores AMD/Intel");

        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoriaEjemplo));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoriaEjemplo);

        Categoria resultado = categoriaService.actualizar(1, cambios);

        assertEquals("Procesadores AMD/Intel", resultado.getNombre());
    }

    @Test
    @DisplayName("eliminar() debe retornar mensaje de exito al eliminar una categoria existente")
    void eliminar_conIdExistente_retornaMensajeExito() {
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoriaEjemplo));
        doNothing().when(categoriaRepository).delete(categoriaEjemplo);

        String resultado = categoriaService.eliminar(1);

        assertEquals("Categoria eliminada correctamente", resultado);
        verify(categoriaRepository, times(1)).delete(categoriaEjemplo);
    }

    @Test
    @DisplayName("eliminar() debe lanzar RuntimeException cuando la categoria no existe")
    void eliminar_conIdInexistente_lanzaException() {
        when(categoriaRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> categoriaService.eliminar(99));
        verify(categoriaRepository, never()).delete(any());
    }
}
