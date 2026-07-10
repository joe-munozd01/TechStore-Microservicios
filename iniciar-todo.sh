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
echo "Iniciando ms_despacho (8089)..."
(cd ms_despacho && .mvnw spring-boot:run) &
echo "Iniciando ms_resena (8086)..."
(cd ms_resena && .mvnw spring-boot:run) &
echo "Iniciando ms_notificacion (8087)..."
(cd ms_notificacion && .mvnw spring-boot:run) &
echo "Iniciando ms_soporte (8088)..."
(cd ms_soporte && .mvnw spring-boot:run) &

echo "Iniciando techstore-gateway (8090)..."
(cd techstore-gateway && ./mvnw spring-boot:run) &

wait
