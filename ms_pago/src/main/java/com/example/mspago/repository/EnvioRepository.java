package com.example.mspago.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.mspago.model.Envio;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Integer> {
}
