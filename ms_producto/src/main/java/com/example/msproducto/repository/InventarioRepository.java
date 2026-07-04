package com.example.msproducto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.msproducto.model.Inventario;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Integer> {
}
