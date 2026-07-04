package com.example.msorden.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.msorden.model.Orden;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Integer> {
}
