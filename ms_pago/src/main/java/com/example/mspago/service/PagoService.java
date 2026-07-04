package com.example.mspago.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.mspago.DTO.PagoDTO;
import com.example.mspago.model.Pago;
import com.example.mspago.repository.PagoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    public List<PagoDTO> obtenerTodos() {
        log.info("Buscando todos los pagos en la base de datos...");
        return pagoRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public PagoDTO buscarPorId(Integer id) {
        log.info("Buscando pago con el ID: {}", id);
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: El pago con ID {} no existe en la base de datos", id);
                    return new RuntimeException("Pago no encontrado");
                });

        return convertirADTO(pago);
    }

    public Pago actualizar(Integer id, Pago pagoActualizado) {
        log.info("Iniciando actualizacion del pago con ID: {}", id);
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se puede actualizar. Pago ID {} no existe", id);
                    return new RuntimeException("Pago no encontrado");
                });
        if (pagoActualizado.getMetodoPago() != null) {
            pago.setMetodoPago(pagoActualizado.getMetodoPago());
        }
        if (pagoActualizado.getMonto() != null) {
            pago.setMonto(pagoActualizado.getMonto());
        }

        log.info("Pago ID: {} actualizado exitosamente", id);
        return pagoRepository.save(pago);
    }

    public String eliminar(Integer id) {
        log.info("Intentando eliminar pago con ID: {}", id);
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se puede eliminar. Pago ID {} no existe", id);
                    return new RuntimeException("Pago no encontrado");
                });
        pagoRepository.delete(pago);
        log.info("Pago ID: {} eliminado correctamente", id);
        return "Pago eliminado correctamente";
    }

    public Pago guardar(Pago pago) {
        log.info("Procesando un nuevo pago con método: {}", pago.getMetodoPago());

        try {
            log.info("Consultando valor actual del Dólar en mindicador.cl...");
            WebClient webClient = WebClient.create("https://mindicador.cl/api");

            String respuestaApi = webClient.get()
                    .uri("/dolar")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("Dólar consultado exitosamente: {}", respuestaApi != null
                    ? respuestaApi.substring(0, Math.min(100, respuestaApi.length())) + "..."
                    : "sin respuesta");

        } catch (Exception e) {
            log.warn("No se pudo consultar la API del dólar, pero el pago continuará. Error: {}", e.getMessage());
        }

        log.info("Guardando pago en la base de datos...");
        return pagoRepository.save(pago);
    }

    private PagoDTO convertirADTO(Pago pago) {

        PagoDTO dto = new PagoDTO();

        dto.setId(pago.getId());
        dto.setMetodoPago(pago.getMetodoPago());
        dto.setMonto(pago.getMonto());
        dto.setIdOrden(pago.getIdOrden());

        return dto;
    }
}
