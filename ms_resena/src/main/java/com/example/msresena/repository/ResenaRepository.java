package com.example.msresena.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.msresena.model.Resena;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Integer> {
    List<Resena> findByIdProducto(Integer idProducto);
    List<Resena> findByIdUsuario(Integer idUsuario);
}
