package com.example.msusuario.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.msusuario.DTO.UsuarioDTO;
import com.example.msusuario.model.Usuario;
import com.example.msusuario.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<UsuarioDTO> obtenerTodos() {
        log.info("Consultando todos los usuarios en la base de datos");
        return usuarioRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public UsuarioDTO buscarPorId(Integer id) {
        log.info("Buscando usuario por el ID {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se encontro el usuario por el ID {}", id);
                    return new RuntimeException("Usuario no encontrado");
                });

        return convertirADTO(usuario);
    }

    public Usuario actualizar(Integer id, Usuario datosActualizados) {
        log.info("Iniciando actualizacion del usuario por el ID {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se puede actualizar. Usuario ID {} no existe", id);
                    return new RuntimeException("Usuario no encontrado");
                });
        if (datosActualizados.getNombre() != null) usuario.setNombre(datosActualizados.getNombre());
        if (datosActualizados.getCorreo() != null) usuario.setCorreo(datosActualizados.getCorreo());
        if (datosActualizados.getPassword() != null && !datosActualizados.getPassword().isBlank()) {
            usuario.setPassword(datosActualizados.getPassword());
        }

        log.info("Usuario ID: {} Actualizado exitosamente", id);
        return usuarioRepository.save(usuario);
    }

    public String eliminar(Integer id) {
        log.info("Intentando eliminar usuario con ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se puede eliminar. Usuario ID {} no existe", id);
                    return new RuntimeException("Usuario no encontrado");
                });
        usuarioRepository.delete(usuario);
        log.info("Usuario ID: {} eliminado correctamente", id);
        return "Usuario eliminado correctamente";
    }

    public Usuario guardar(Usuario usuario) {
        log.info("Guardando un nuevo usuario...");
        return usuarioRepository.save(usuario);
    }

    private UsuarioDTO convertirADTO(Usuario usuario) {

        UsuarioDTO dto = new UsuarioDTO();

        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setCorreo(usuario.getCorreo());

        return dto;
    }
}
