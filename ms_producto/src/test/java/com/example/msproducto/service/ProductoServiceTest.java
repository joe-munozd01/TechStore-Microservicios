package com.example.msproducto.service;

import com.example.msproducto.DTO.ProductoDTO;
import com.example.msproducto.model.Categoria;
import com.example.msproducto.model.Marca;
import com.example.msproducto.model.Producto;
import com.example.msproducto.repository.CategoriaRepository;
import com.example.msproducto.repository.MarcaRepository;
import com.example.msproducto.repository.ProductoRepository;
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
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private MarcaRepository marcaRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto productoEjemplo;
    private Categoria categoriaEjemplo;
    private Marca marcaEjemplo;

    @BeforeEach
    void setUp() {
        categoriaEjemplo = new Categoria();
        categoriaEjemplo.setId(1);
        categoriaEjemplo.setNombre("Procesadores");

        marcaEjemplo = new Marca();
        marcaEjemplo.setId(1);
        marcaEjemplo.setNombre("AMD");

        productoEjemplo = new Producto();
        productoEjemplo.setId(1);
        productoEjemplo.setNombre("Ryzen 5 5600X");
        productoEjemplo.setDescripcion("Procesador 6 nucleos");
        productoEjemplo.setPrecio(120000);
        productoEjemplo.setCategoria(categoriaEjemplo);
        productoEjemplo.setMarca(marcaEjemplo);
    }

    @Test
    @DisplayName("obtenerTodos() debe retornar lista de ProductoDTO con nombre de categoria y marca")
    void obtenerTodos_conProductosExistentes_retornaListaDTO() {
        when(productoRepository.findAll()).thenReturn(List.of(productoEjemplo));

        List<ProductoDTO> resultado = productoService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Ryzen 5 5600X", resultado.get(0).getNombre());
        assertEquals("Procesadores", resultado.get(0).getNombreCategoria());
        assertEquals("AMD", resultado.get(0).getNombreMarca());
    }

    @Test
    @DisplayName("buscarPorId() debe lanzar RuntimeException cuando el producto no existe")
    void buscarPorId_conIdInexistente_lanzaException() {
        when(productoRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> productoService.buscarPorId(99));
        assertEquals("Producto no encontrado", ex.getMessage());
    }

    @Test
    @DisplayName("guardarProducto() debe asociar categoria y marca correctamente")
    void guardarProducto_datosValidos_asociaCategoriaYMarca() {
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("RTX 4060");
        dto.setDescripcion("Tarjeta grafica gama media");
        dto.setPrecio(450000);
        dto.setIdCategoria(1);
        dto.setIdMarca(1);

        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoriaEjemplo));
        when(marcaRepository.findById(1)).thenReturn(Optional.of(marcaEjemplo));
        when(productoRepository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        Producto resultado = productoService.guardarProducto(dto);

        assertEquals("RTX 4060", resultado.getNombre());
        assertEquals(categoriaEjemplo, resultado.getCategoria());
        assertEquals(marcaEjemplo, resultado.getMarca());
    }

    @Test
    @DisplayName("guardarProducto() debe lanzar excepcion si la categoria no existe")
    void guardarProducto_categoriaInexistente_lanzaExcepcion() {
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("RTX 4060");
        dto.setIdCategoria(99);
        dto.setIdMarca(1);

        when(categoriaRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> productoService.guardarProducto(dto));
        assertEquals("Error: La categoria no existe", ex.getMessage());
        verify(productoRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizarProducto() debe modificar solo los campos proporcionados")
    void actualizarProducto_soloPrecio_actualizaCorrectamente() {
        Producto cambios = new Producto();
        cambios.setPrecio(99990);

        when(productoRepository.findById(1)).thenReturn(Optional.of(productoEjemplo));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoEjemplo);

        Producto resultado = productoService.actualizarProducto(1, cambios);

        assertEquals(99990, resultado.getPrecio());
        assertEquals("Ryzen 5 5600X", resultado.getNombre(), "El nombre no debe cambiar");
    }

    @Test
    @DisplayName("eliminar() debe retornar mensaje de exito para producto existente")
    void eliminar_conIdExistente_retornaMensajeExito() {
        when(productoRepository.findById(1)).thenReturn(Optional.of(productoEjemplo));
        doNothing().when(productoRepository).delete(productoEjemplo);

        String resultado = productoService.eliminar(1);

        assertEquals("Producto eliminado correctamente", resultado);
        verify(productoRepository, times(1)).delete(productoEjemplo);
    }

    @Test
    @DisplayName("eliminar() debe retornar el mensaje de error cuando el producto no existe")
    void eliminar_conIdInexistente_retornaMensajeError() {
        when(productoRepository.findById(99)).thenReturn(Optional.empty());

        String resultado = productoService.eliminar(99);

        assertEquals("Producto no encontrado", resultado);
        verify(productoRepository, never()).delete(any());
    }
}
