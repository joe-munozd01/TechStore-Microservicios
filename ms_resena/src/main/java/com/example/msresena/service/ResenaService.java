package com.example.msresena.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.msresena.DTO.ResenaDTO;
import com.example.msresena.client.ProductoClient;
import com.example.msresena.model.Resena;
import com.example.msresena.repository.ResenaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ResenaService {

    @Autowired
    private ProductoClient productoClient;

    @Autowired
    private ResenaRepository resenaRepository;

    public List<ResenaDTO> obtenerTodas() {
        log.info("Consultando todas las reseñas");
        return resenaRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public ResenaDTO buscarPorId(Integer id) {
        Resena resena = resenaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));
        return convertirADTO(resena);
    }

    public List<ResenaDTO> buscarPorProducto(Integer idProducto) {
        return resenaRepository.findByIdProducto(idProducto).stream()
                .map(this::convertirADTO)
                .toList();
    }

    public List<ResenaDTO> buscarPorUsuario(Integer idUsuario) {
        return resenaRepository.findByIdUsuario(idUsuario).stream()
                .map(this::convertirADTO)
                .toList();
    }

    public Resena guardar(Resena resena) {
        productoClient.obtenerProductoPorId(resena.getIdProducto()); 
        resena.setFechaCreacion(java.time.LocalDateTime.now());
        return resenaRepository.save(resena);
}

    public Resena actualizar(Integer id, Resena datosActualizados) {
        Resena resena = resenaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));

        if (datosActualizados.getCalificacion() != null) resena.setCalificacion(datosActualizados.getCalificacion());
        if (datosActualizados.getComentario() != null) resena.setComentario(datosActualizados.getComentario());

        return resenaRepository.save(resena);
    }

    public String eliminar(Integer id) {
        Resena resena = resenaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));
        resenaRepository.delete(resena);
        return "Reseña eliminada correctamente";
    }

    private ResenaDTO convertirADTO(Resena resena) {
        ResenaDTO dto = new ResenaDTO();
        dto.setId(resena.getId());
        dto.setIdUsuario(resena.getIdUsuario());
        dto.setIdProducto(resena.getIdProducto());
        dto.setCalificacion(resena.getCalificacion());
        dto.setComentario(resena.getComentario());
        dto.setFechaCreacion(resena.getFechaCreacion());
        return dto;
    }
}
