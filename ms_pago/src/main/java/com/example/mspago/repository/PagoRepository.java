package com.example.mspago.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.mspago.model.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {
}
