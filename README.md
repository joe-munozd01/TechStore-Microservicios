# TechStore - Backend en Microservicios

Proyecto final para la asignatura Desarrollo FullStack 1 (Duoc UC). 
Este proyecto consiste en la migración de un sistema monolítico a una arquitectura moderna orientada a microservicios, asegurando alta disponibilidad, escalabilidad y un despliegue automatizado.

### Integrantes
* Lalo Muñoz
* Felipe Baez

### Arquitectura del Sistema
El ecosistema está compuesto por 10 contenedores orquestados que interactúan entre sí de forma transparente:

* API Gateway (Puerto 8090): Punto único de entrada, balanceo de carga y enrutamiento dinámico.
* Eureka Server (Puerto 8761): Service Discovery para el registro y localización de los microservicios.
* 8 Microservicios de Negocio: ms_usuario, ms_producto, ms_orden, ms_pago, ms_despacho, ms_notificacion, ms_resena y ms_soporte.
* Persistencia Aislada: Un servidor MariaDB que inicializa bases de datos independientes para cada microservicio, garantizando el desacoplamiento de datos.

### Stack Tecnológico
* Backend: Java 21 y Spring Boot 3.
* Spring Cloud: Gateway, Netflix Eureka y OpenFeign (para la comunicación síncrona entre microservicios).
* Base de Datos: Spring Data JPA y MariaDB.
* Testing: Pruebas unitarias con JUnit 5 y Mockito (asegurando más del 80% de cobertura).
* Despliegue: Docker y Docker Compose para la contenerización y orquestación local.

### Despliegue Rápido
Para levantar toda la infraestructura, asegúrese de tener el motor de Docker en ejecución y corra el siguiente comando en la raíz del proyecto:

```bash
docker-compose up --build -d
