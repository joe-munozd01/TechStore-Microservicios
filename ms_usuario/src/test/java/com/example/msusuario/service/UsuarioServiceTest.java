package com.example.msusuario.service;

import com.example.msusuario.DTO.UsuarioDTO;
import com.example.msusuario.model.Usuario;
import com.example.msusuario.repository.UsuarioRepository;
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
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioEjemplo;

    @BeforeEach
    void setUp() {
        usuarioEjemplo = new Usuario();
        usuarioEjemplo.setId(1);
        usuarioEjemplo.setNombre("Felipe Baez");
        usuarioEjemplo.setCorreo("felipe@techstore.cl");
        usuarioEjemplo.setPassword("clave123");
    }

    @Test
    @DisplayName("obtenerTodos() debe retornar lista de UsuarioDTO")
    void obtenerTodos_conUsuariosExistentes_retornaListaDTO() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioEjemplo));

        List<UsuarioDTO> resultado = usuarioService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Felipe Baez", resultado.get(0).getNombre());
        assertEquals("felipe@techstore.cl", resultado.get(0).getCorreo());
    }

    @Test
    @DisplayName("buscarPorId() debe retornar UsuarioDTO cuando el ID existe")
    void buscarPorId_conIdExistente_retornaDTO() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioEjemplo));

        UsuarioDTO resultado = usuarioService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Felipe Baez", resultado.getNombre());
    }

    @Test
    @DisplayName("buscarPorId() debe lanzar RuntimeException cuando el usuario no existe")
    void buscarPorId_conIdInexistente_lanzaException() {
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.buscarPorId(99));
        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    @Test
    @DisplayName("actualizar() debe modificar solo los campos proporcionados")
    void actualizar_conNuevoNombre_actualizaCorrectamente() {
        Usuario cambios = new Usuario();
        cambios.setNombre("Felipe Baez Actualizado");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioEjemplo));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioEjemplo);

        Usuario resultado = usuarioService.actualizar(1, cambios);

        assertEquals("Felipe Baez Actualizado", resultado.getNombre());
        assertEquals("felipe@techstore.cl", resultado.getCorreo(), "El correo no debe cambiar");
        verify(usuarioRepository, times(1)).save(usuarioEjemplo);
    }

    @Test
    @DisplayName("actualizar() debe lanzar RuntimeException cuando el usuario no existe")
    void actualizar_conIdInexistente_lanzaException() {
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.actualizar(99, new Usuario()));
        assertEquals("Usuario no encontrado", ex.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("eliminar() debe retornar mensaje de éxito para usuario existente")
    void eliminar_conIdExistente_retornaMensajeExito() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioEjemplo));
        doNothing().when(usuarioRepository).delete(usuarioEjemplo);

        String resultado = usuarioService.eliminar(1);

        assertEquals("Usuario eliminado correctamente", resultado);
        verify(usuarioRepository, times(1)).delete(usuarioEjemplo);
    }

    @Test
    @DisplayName("eliminar() debe lanzar RuntimeException cuando el usuario no existe")
    void eliminar_conIdInexistente_lanzaException() {
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.eliminar(99));
        verify(usuarioRepository, never()).delete(any());
    }

    @Test
    @DisplayName("guardar() debe persistir el usuario correctamente")
    void guardar_usuarioValido_retornaGuardado() {
        Usuario nuevo = new Usuario();
        nuevo.setNombre("Ana Torres");
        nuevo.setCorreo("ana@techstore.cl");
        nuevo.setPassword("segura123");

        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(5);
            return u;
        });

        Usuario resultado = usuarioService.guardar(nuevo);

        assertNotNull(resultado);
        assertEquals(5, resultado.getId());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }
}
