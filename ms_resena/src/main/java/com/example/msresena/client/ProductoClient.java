package com.example.msresena.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-producto")
public interface ProductoClient {
    @GetMapping("/api/v1/productos/{id}")
    Object obtenerProductoPorId(@PathVariable("id") Integer id);
}
