package com.example.gateway;

import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

/**
 * Filtro global del API Gateway: valida el JWT y aplica el control de acceso por rol.
 *
 * Publico (sin token): login/registro, Swagger, y GET del catalogo (productos, categorias, marcas, resenas).
 * Solo ADMIN: todo /usuarios, y cualquier escritura (POST/PUT/DELETE) sobre el catalogo e inventario.
 * Cualquier otra ruta: requiere un token valido (ADMIN o CLIENTE).
 */
@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private static final List<String> CATALOGO = List.of(
            "/api/v1/productos", "/api/v1/categorias", "/api/v1/marcas", "/api/v1/inventarios");
    private static final List<String> LECTURA_PUBLICA = List.of(
            "/api/v1/productos", "/api/v1/categorias", "/api/v1/marcas", "/api/v1/resenas");

    private final SecretKey clave;

    public JwtAuthFilter(@Value("${jwt.secret}") String secreto) {
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        HttpMethod metodo = exchange.getRequest().getMethod();

        if (HttpMethod.OPTIONS.equals(metodo) || esPublica(path, metodo)) {
            return chain.filter(exchange);
        }

        String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            return error(exchange, HttpStatus.UNAUTHORIZED, "Falta el token de autenticacion");
        }

        Claims claims;
        try {
            claims = Jwts.parser().verifyWith(clave).build()
                    .parseSignedClaims(header.substring(7)).getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            return error(exchange, HttpStatus.UNAUTHORIZED, "Token invalido o expirado");
        }

        String rol = claims.get("rol", String.class);
        if (requiereAdmin(path, metodo) && !"ADMIN".equals(rol)) {
            return error(exchange, HttpStatus.FORBIDDEN, "No tienes permisos para esta accion");
        }

        // Se informa a los microservicios quien hace la peticion.
        var request = exchange.getRequest().mutate()
                .header("X-User-Email", claims.getSubject())
                .header("X-User-Rol", rol == null ? "" : rol)
                .build();
        return chain.filter(exchange.mutate().request(request).build());
    }

    private boolean esPublica(String path, HttpMethod metodo) {
        if (path.startsWith("/api/v1/auth/") || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs") || path.contains("/api-docs")) {
            return true;
        }
        return HttpMethod.GET.equals(metodo) && LECTURA_PUBLICA.stream().anyMatch(path::startsWith);
    }

    private boolean requiereAdmin(String path, HttpMethod metodo) {
        if (path.startsWith("/api/v1/usuarios")) return true;
        boolean escritura = !HttpMethod.GET.equals(metodo);
        return escritura && CATALOGO.stream().anyMatch(path::startsWith);
    }

    private Mono<Void> error(ServerWebExchange exchange, HttpStatus status, String mensaje) {
        var response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBuffer buffer = response.bufferFactory()
                .wrap(("{\"mensaje\":\"" + mensaje + "\"}").getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
