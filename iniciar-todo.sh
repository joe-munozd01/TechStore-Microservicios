#!/bin/bash
# Levanta todo el ecosistema de TechStore en orden: eureka -> microservicios -> gateway
set -e

echo "Iniciando techstore-eureka (8761)..."
(cd techstore-eureka && ./mvnw spring-boot:run) &
sleep 15

echo "Iniciando ms_usuario (8081)..."
(cd ms_usuario && ./mvnw spring-boot:run) &
echo "Iniciando ms_producto (8082)..."
(cd ms_producto && ./mvnw spring-boot:run) &
echo "Iniciando ms_orden (8083)..."
(cd ms_orden && ./mvnw spring-boot:run) &
echo "Iniciando ms_pago (8084)..."
(cd ms_pago && ./mvnw spring-boot:run) &
sleep 20

echo "Iniciando techstore-gateway (8090)..."
(cd techstore-gateway && ./mvnw spring-boot:run) &

wait
