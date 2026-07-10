package com.example.msresena.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.msresena.DTO.ResenaDTO;
import com.example.msresena.client.ProductoClient;
import com.example.msresena.model.Resena;
import com.example.msresena.repository.ResenaRepository;

@ExtendWith(MockitoExtension.class)
public class ResenaServiceTest {

    @Mock
    private ResenaRepository resenaRepository;

    @Mock 
    private ProductoClient productoClient;

    @InjectMocks
    private ResenaService resenaService;

    private Resena resenaEjemplo;

    @BeforeEach
    void setUp() {
        resenaEjemplo = new Resena();
        resenaEjemplo.setId(1);
        resenaEjemplo.setIdProducto(10);
        resenaEjemplo.setIdUsuario(5);
        resenaEjemplo.setCalificacion(5);
        resenaEjemplo.setComentario("Excelente producto");
    }

    @Test
    void obtenerTodas_DebeRetornarLista() {
        when(resenaRepository.findAll()).thenReturn(List.of(resenaEjemplo));
        List<ResenaDTO> resultado = resenaService.obtenerTodas();
        assertFalse(resultado.isEmpty());
        assertEquals("Excelente producto", resultado.get(0).getComentario());
    }

    @Test
    void guardar_CuandoProductoExiste_DebeGuardarResena() {
        when(productoClient.obtenerProductoPorId(any(Integer.class))).thenReturn(new Object());
        when(resenaRepository.save(any(Resena.class))).thenReturn(resenaEjemplo);

        Resena resultado = resenaService.guardar(resenaEjemplo);

        assertNotNull(resultado);
        assertEquals(5, resultado.getCalificacion());
        verify(productoClient, times(1)).obtenerProductoPorId(any(Integer.class));
    }

    @Test
    void guardar_CuandoProductoNoExiste_DebeLanzarExcepcion() {
        when(productoClient.obtenerProductoPorId(any(Integer.class))).thenThrow(new RuntimeException("Not Found"));
        
        assertThrows(RuntimeException.class, () -> {
            resenaService.guardar(resenaEjemplo);
        });
    }
}