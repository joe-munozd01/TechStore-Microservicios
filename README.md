# TechStore - Backend en Microservicios

Proyecto final para la asignatura **Desarrollo FullStack 1** (Duoc UC).

**Integrantes:**
- José Muñoz
- Felipe Baez

## Resumen
Este repositorio contiene el backend de nuestra tienda (TechStore). Pasamos el proyecto de una arquitectura monolítica a microservicios independientes usando Spring Boot y Spring Cloud.

## Tecnologías
- Java 21
- Spring Boot
- Maven
- MySQL 8 (vía Laragon/XAMPP)
- Lombok (v1.18.32)

## Arquitectura y Puertos
El sistema levanta varios servidores en paralelo. Esta es la distribución:

- **Eureka (Service Registry):** `localhost:8761` (Monitor donde se registran los servicios)
- **API Gateway:** `localhost:8090` (Puerta de entrada principal)
- **MS Usuarios:** `localhost:8081`
- **MS Productos:** `localhost:8082`
- **MS Órdenes:** `localhost:8083`
- **MS Pagos:** `localhost:8084`

## Instrucciones para ejecutar (Local)

Para no tener que levantar cada microservicio a mano, dejamos un script que automatiza el arranque.

1. Enciende tu motor de base de datos MySQL (Laragon o XAMPP) en el puerto `3306`. (Credenciales por defecto: usuario `root`, sin contraseña).
2. Asegúrate de tener creada la base de datos `techstore_db`.
3. Haz doble clic en el archivo `iniciar-todo.bat`. (Si revisas desde Mac/Linux, usa la terminal y corre `./iniciar-todo.sh`).
4. Se abrirán varias consolas. Espera unos segundos a que compilen.
5. Abre tu navegador y entra a `http://localhost:8761`. Cuando veas que todos los servicios aparecen en verde con el texto **UP**, el backend está completamente operativo.

## Probar las APIs (Swagger)
Como el proyecto está dividido, cada microservicio tiene su propia documentación y panel de pruebas. Una vez que el sistema esté levantado, puedes probar los endpoints (GET, POST, etc.) en estos enlaces:

- **Usuarios:** http://localhost:8081/swagger-ui/index.html
- **Productos:** http://localhost:8082/swagger-ui/index.html
- **Órdenes:** http://localhost:8083/swagger-ui/index.html
- **Pagos:** http://localhost:8084/swagger-ui/index.html
