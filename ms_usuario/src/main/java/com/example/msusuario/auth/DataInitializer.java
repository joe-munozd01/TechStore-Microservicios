package com.example.msusuario.auth;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import com.example.msusuario.model.Usuario;
import com.example.msusuario.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;

/** Crea al iniciar las cuentas de prueba si no existen (mismas que usa el frontend). */
@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository repo;

    public DataInitializer(UsuarioRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) {
        crear("Administrador", "admin@techstore.cl", "Admin123", "ADMIN");
        crear("Cliente Demo", "cliente@techstore.cl", "Cliente123", "CLIENTE");
    }

    private void crear(String nombre, String correo, String clave, String rol) {
        if (repo.existsByCorreo(correo)) return;
        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setCorreo(correo);
        u.setPassword(new BCryptPasswordEncoder().encode(clave));
        u.setRol(rol);
        repo.save(u);
        log.info("Usuario inicial creado: {} ({})", correo, rol);
    }
}
