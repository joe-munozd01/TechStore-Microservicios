package com.example.msusuario.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.msusuario.DTO.UsuarioDTO;
import com.example.msusuario.auth.AuthDTOs.AuthResponse;
import com.example.msusuario.auth.AuthDTOs.LoginRequest;
import com.example.msusuario.auth.AuthDTOs.RegistroRequest;
import com.example.msusuario.model.Usuario;
import com.example.msusuario.repository.UsuarioRepository;
import com.example.msusuario.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthService {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    /** Devuelve null si las credenciales son inválidas. */
    public AuthResponse login(LoginRequest req) {
        Usuario u = usuarioRepository.findByCorreo(req.correo().trim().toLowerCase()).orElse(null);
        if (u == null || !passwordCoincide(u, req.password())) {
            log.warn("Login fallido para {}", req.correo());
            return null;
        }
        log.info("Login correcto: {} ({})", u.getCorreo(), u.getRol());
        return respuesta(u);
    }

    /** Registra siempre con rol CLIENTE (el rol ADMIN no se puede auto-asignar). */
    public AuthResponse registrar(RegistroRequest req) {
        String correo = req.correo().trim().toLowerCase();
        if (usuarioRepository.existsByCorreo(correo)) {
            throw new IllegalStateException("El correo ya está registrado");
        }
        Usuario u = new Usuario();
        u.setNombre(req.nombre().trim());
        u.setCorreo(correo);
        u.setPassword(ENCODER.encode(req.password()));
        u.setRol("CLIENTE");
        return respuesta(usuarioRepository.save(u));
    }

    private boolean passwordCoincide(Usuario u, String plana) {
        String guardada = u.getPassword();
        if (guardada.startsWith("$2")) {
            return ENCODER.matches(plana, guardada);
        }
        // Usuarios antiguos con clave en texto plano: se valida y se migra a BCrypt.
        if (guardada.equals(plana)) {
            u.setPassword(ENCODER.encode(plana));
            usuarioRepository.save(u);
            return true;
        }
        return false;
    }

    private AuthResponse respuesta(Usuario u) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(u.getId());
        dto.setNombre(u.getNombre());
        dto.setCorreo(u.getCorreo());
        dto.setRol(u.getRol());
        return new AuthResponse(jwtUtil.generar(u), dto);
    }
}
