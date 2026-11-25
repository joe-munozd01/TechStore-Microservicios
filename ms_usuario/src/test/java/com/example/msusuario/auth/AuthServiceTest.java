package com.example.msusuario.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import com.example.msusuario.auth.AuthDTOs.AuthResponse;
import com.example.msusuario.auth.AuthDTOs.LoginRequest;
import com.example.msusuario.auth.AuthDTOs.RegistroRequest;
import com.example.msusuario.model.Usuario;
import com.example.msusuario.repository.UsuarioRepository;
import com.example.msusuario.security.JwtUtil;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository repo;

    @InjectMocks
    private AuthService authService;

    private Usuario admin;

    @BeforeEach
    void setUp() {
        // JwtUtil real (no simulado): asi se prueba tambien que el token se genera.
        ReflectionTestUtils.setField(authService, "jwtUtil",
                new JwtUtil("secreto-de-prueba-con-mas-de-32-caracteres-123", 60));
        admin = new Usuario();
        admin.setId(1);
        admin.setNombre("Administrador");
        admin.setCorreo("admin@techstore.cl");
        admin.setPassword(new BCryptPasswordEncoder().encode("Admin123"));
        admin.setRol("ADMIN");
    }

    @Test
    @DisplayName("login() con credenciales correctas devuelve token y rol")
    void login_correcto() {
        when(repo.findByCorreo("admin@techstore.cl")).thenReturn(Optional.of(admin));

        AuthResponse r = authService.login(new LoginRequest("admin@techstore.cl", "Admin123"));

        assertNotNull(r);
        assertEquals(3, r.token().split("\\.").length, "el token debe ser un JWT (3 partes)");
        assertEquals("ADMIN", r.usuario().getRol());
    }

    @Test
    @DisplayName("login() con clave incorrecta devuelve null")
    void login_claveIncorrecta() {
        when(repo.findByCorreo("admin@techstore.cl")).thenReturn(Optional.of(admin));
        assertNull(authService.login(new LoginRequest("admin@techstore.cl", "otra")));
    }

    @Test
    @DisplayName("login() con usuario inexistente devuelve null")
    void login_usuarioInexistente() {
        when(repo.findByCorreo("x@x.cl")).thenReturn(Optional.empty());
        assertNull(authService.login(new LoginRequest("x@x.cl", "123456")));
    }

    @Test
    @DisplayName("login() migra una clave antigua en texto plano a BCrypt")
    void login_migraClavePlana() {
        admin.setPassword("plana123");
        when(repo.findByCorreo("admin@techstore.cl")).thenReturn(Optional.of(admin));

        assertNotNull(authService.login(new LoginRequest("admin@techstore.cl", "plana123")));
        assertTrue(admin.getPassword().startsWith("$2"));
        verify(repo).save(admin);
    }

    @Test
    @DisplayName("registrar() crea siempre un CLIENTE con clave cifrada")
    void registrar_creaCliente() {
        when(repo.existsByCorreo("nuevo@techstore.cl")).thenReturn(false);
        when(repo.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        AuthResponse r = authService.registrar(new RegistroRequest("Nuevo User", "Nuevo@TechStore.cl", "clave123"));

        assertEquals("CLIENTE", r.usuario().getRol());
        assertEquals("nuevo@techstore.cl", r.usuario().getCorreo());
        verify(repo).save(argThat(u -> u.getPassword().startsWith("$2")));
    }

    @Test
    @DisplayName("registrar() con correo repetido lanza excepcion")
    void registrar_correoRepetido() {
        when(repo.existsByCorreo("admin@techstore.cl")).thenReturn(true);
        assertThrows(IllegalStateException.class,
                () -> authService.registrar(new RegistroRequest("Otro Admin", "admin@techstore.cl", "clave123")));
    }
}
