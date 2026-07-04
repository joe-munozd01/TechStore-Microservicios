# TechStore - Microservicios

Migración del monolito **TechStore** (Spring Boot + JPA) a una arquitectura de
microservicios, usando como referencia estructural el proyecto **FitZone v2**
(Eureka + Gateway + microservicios independientes, cada uno con su propio
`pom.xml`, tests y base de datos).

```
Cliente → techstore-gateway (8090) → techstore-eureka (8761)
                                  ↓
        ┌───────────────┬────────────────┬───────────────┐
    ms-usuario       ms-producto      ms-orden        ms-pago
      (8081)           (8082)          (8083)          (8084)
    Usuario         Producto         Carrito           Pago
                    Categoria        CarritoDetalle    Envio
                    Marca            Orden
                    Inventario       DetalleOrden
```

## ¿Por qué esta agrupación y no `ms_entrenamiento` / `ms_membresia` / `ms_sucursal`?

FitZone es un gimnasio, por eso sus microservicios se llaman así. TechStore es
una tienda de tecnología y no tiene esas entidades en su dominio, así que se
agruparon las 11 entidades reales del monolito (`Usuario`, `Producto`,
`Categoria`, `Marca`, `Inventario`, `Carrito`, `CarritoDetalle`, `Orden`,
`DetalleOrden`, `Pago`, `Envio`) en 4 microservicios equivalentes en número y
en patrón (Eureka + Gateway + N servicios independientes):

| Servicio | Puerto | Entidades | Base de datos |
|---|---|---|---|
| `techstore-eureka` | 8761 | — | — |
| `techstore-gateway` | 8090 | — | — |
| `ms_usuario` | 8081 | Usuario | ms_usuario_db |
| `ms_producto` | 8082 | Producto, Categoria, Marca, Inventario | ms_producto_db |
| `ms_orden` | 8083 | Carrito, CarritoDetalle, Orden, DetalleOrden | ms_orden_db |
| `ms_pago` | 8084 | Pago, Envio | ms_pago_db |

## Relaciones entre entidades: qué cambió y por qué

En el monolito, muchas entidades estaban unidas por relaciones JPA
(`@ManyToOne`, `@OneToOne`) que hacían *join* directo en la misma base de
datos. Al separar en microservicios con **bases de datos independientes**,
esos joins ya no son posibles: cada microservicio solo puede tener relaciones
JPA con entidades que sigan viviendo en su propia base de datos.

- **Relaciones que se mantienen** (ambas entidades quedaron en el mismo
  microservicio): `Producto`↔`Categoria`, `Producto`↔`Marca`,
  `Producto`↔`Inventario` (en `ms_producto`); `Carrito`↔`CarritoDetalle`,
  `Orden`↔`DetalleOrden` (en `ms_orden`).
- **Relaciones que se reemplazaron por un ID simple** (las entidades quedaron
  en microservicios distintos): `Carrito.usuario` → `Carrito.idUsuario`,
  `Orden.usuario` → `Orden.idUsuario`, `CarritoDetalle.producto` →
  `CarritoDetalle.idProducto`, `DetalleOrden.producto` →
  `DetalleOrden.idProducto`, `Pago.orden` → `Pago.idOrden`, `Envio.orden` →
  `Envio.idOrden`. El campo `Orden.pago` (`@OneToOne` inverso) se eliminó de
  `Orden`, ya que ahora es `ms_pago` quien guarda su propio `idOrden`.

Esto es exactamente el mismo patrón que usa FitZone (`Usuario.idMembresia`,
`Usuario.idSucursal` son enteros simples, no relaciones JPA).

**Importante:** con este cambio, los DTOs que antes mostraban datos
enriquecidos de otro microservicio (p. ej. `nombreUsuario` en `OrdenDTO`) ahora
solo exponen el ID (`idUsuario`). Si se necesita mostrar el nombre real,
hay que hacerlo con una llamada HTTP (Feign/RestTemplate/WebClient) desde el
microservicio que lo necesite hacia el que tiene el dato — no está incluido
en esta entrega para no salirse del alcance pedido, pero es el siguiente paso
natural.

## Cómo correr todo

Requisitos: Java 21, Maven, MySQL corriendo en `localhost:3306` (usuario
`root`, sin contraseña).

```bash
cd techstore-eureka   && ./mvnw spring-boot:run   # primero, puerto 8761

cd ms_usuario         && ./mvnw spring-boot:run   # puerto 8081
cd ms_producto        && ./mvnw spring-boot:run   # puerto 8082
cd ms_orden           && ./mvnw spring-boot:run   # puerto 8083
cd ms_pago            && ./mvnw spring-boot:run   # puerto 8084

cd techstore-gateway  && ./mvnw spring-boot:run   # al final, puerto 8090
```

(`iniciar-todo.sh` / `iniciar-todo.bat` levantan todo en orden automáticamente.)

- Eureka dashboard: `http://localhost:8761`
- Gateway (punto único de entrada): `http://localhost:8090`
- Swagger de cada servicio: `http://localhost:PUERTO/swagger-ui/index.html`

## Endpoints principales (a través del gateway, puerto 8090)

- `GET/POST/PUT/DELETE /api/v1/usuarios` → ms_usuario
- `GET/POST/PUT/DELETE /api/v1/productos` → ms_producto
- `GET/POST/PUT/DELETE /api/v1/categorias` → ms_producto
- `GET/POST/PUT/DELETE /api/v1/marcas` → ms_producto
- `GET/POST/PUT/DELETE /api/v1/inventarios` → ms_producto
- `GET/POST/PUT/DELETE /api/v1/carritos` → ms_orden
- `GET/POST/PUT/DELETE /api/v1/carrito-detalle` → ms_orden
- `GET/POST/PUT/DELETE /api/v1/ordenes` → ms_orden
- `GET/POST/PUT/DELETE /api/v1/detalle-orden` → ms_orden
- `GET/POST/PUT/DELETE /api/v1/pagos` → ms_pago
- `GET/POST/PUT/DELETE /api/v1/envios` → ms_pago

## Tests

Cada microservicio incluye tests unitarios (JUnit 5 + Mockito) de sus
servicios, más un test de contexto (`contextLoads`). Se ejecutan igual que en
el monolito original:

```bash
cd ms_usuario && ./mvnw test
cd ms_producto && ./mvnw test
cd ms_orden && ./mvnw test
cd ms_pago && ./mvnw test
```

## Estructura de cada microservicio (igual que FitZone)

```
ms_x/
├── pom.xml
├── mvnw / mvnw.cmd
└── src/
    ├── main/
    │   ├── java/com/example/msx/
    │   │   ├── MsXApplication.java      (@SpringBootApplication + @EnableDiscoveryClient)
    │   │   ├── model/                   (entidades JPA)
    │   │   ├── DTO/
    │   │   ├── repository/              (JpaRepository)
    │   │   ├── service/                 (lógica de negocio)
    │   │   ├── controller/              (REST, /api/v1/...)
    │   │   └── exception/               (GlobalExceptionHandler)
    │   └── resources/application.yml    (puerto, perfiles dev/prod, Eureka, Swagger)
    └── test/java/com/example/msx/
        ├── MsXApplicationTests.java
        └── service/*ServiceTest.java
```
