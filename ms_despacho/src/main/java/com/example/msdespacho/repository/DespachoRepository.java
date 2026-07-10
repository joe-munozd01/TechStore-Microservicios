package com.example.msdespacho.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.msdespacho.model.Despacho;

@Repository
public interface DespachoRepository extends JpaRepository<Despacho, Integer> {
    List<Despacho> findByIdOrden(Integer idOrden);
}
