# TechStore - Backend en Microservicios

Proyecto final para la asignatura Desarrollo FullStack 1 (Duoc UC). 
Este proyecto consiste en la migración de un sistema monolítico a una arquitectura moderna orientada a microservicios, asegurando alta disponibilidad, escalabilidad y un despliegue automatizado.

### Integrantes
* José Muñoz
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


---

## Evaluación Parcial 3 - Integración con el frontend (DSY1104 Fullstack II)
Integrantes: Nicolás Figueroa, José Muñoz.

### Autenticación con JWT y roles
* `POST /api/v1/auth/login` → `{ correo, password }` → devuelve `{ token, usuario: { id, nombre, correo, rol } }`.
* `POST /api/v1/auth/registro` → `{ nombre, correo, password }` → crea un usuario con rol **CLIENTE** y devuelve su token.
* Las contraseñas se guardan cifradas con **BCrypt** (las antiguas en texto plano se migran solas al primer login).
* Al iniciar `ms_usuario` se crean las cuentas de prueba si no existen:
  * `admin@techstore.cl` / `Admin123` (rol ADMIN)
  * `cliente@techstore.cl` / `Cliente123` (rol CLIENTE)

### Control de acceso en el API Gateway (`JwtAuthFilter`)
| Ruta | Acceso |
|---|---|
| `/api/v1/auth/**`, Swagger, `GET` de productos/categorías/marcas/reseñas | Público |
| `/api/v1/usuarios/**` y `POST/PUT/DELETE` de productos, categorías, marcas, inventario | Solo **ADMIN** (si no: 403) |
| Todo lo demás (órdenes, pagos, despachos, etc.) | Cualquier usuario con token válido (si no: 401) |

El token se envía en la cabecera `Authorization: Bearer <token>`. El secreto se configura con la variable `JWT_SECRET` (tiene un valor por defecto para desarrollo, el mismo en gateway y ms_usuario).

### CORS
El gateway acepta peticiones del frontend en `http://localhost:3000`.

### Notas para Linux (Fedora)
* En versiones nuevas de Docker el comando es `docker compose` (con espacio) en vez de `docker-compose`.
* El volumen `./init-db` lleva `:z` en `docker-compose.yml` para que SELinux permita a MariaDB leer el script de las bases de datos.
* Si un servicio no aparece en el gateway (error 503), esperar 1-2 minutos a que se registre en Eureka (http://localhost:8761).
* Para borrar todo y partir de cero (incluye la base de datos): `docker compose down -v`.

### Documentación Swagger
Con todo levantado: http://localhost:8090/swagger-ui/index.html (selector arriba a la derecha para ver cada microservicio).

### Levantar en local sin Docker
**Linux (Fedora):** tener MariaDB corriendo en el puerto 3306 con usuario `root` sin contraseña, y luego `./iniciar-todo.sh`.
**Windows:** lo mismo con `iniciar-todo.bat`.
Las bases de datos se crean solas (`createDatabaseIfNotExist=true`).
