package com.example.msproducto.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.msproducto.DTO.ProductoDTO;
import com.example.msproducto.model.Categoria;
import com.example.msproducto.model.Marca;
import com.example.msproducto.model.Producto;
import com.example.msproducto.repository.CategoriaRepository;
import com.example.msproducto.repository.MarcaRepository;
import com.example.msproducto.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final MarcaRepository marcaRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository,
                            MarcaRepository marcaRepository,
                            CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.marcaRepository = marcaRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<ProductoDTO> obtenerTodos() {
        log.info("Consultando todos los productos en la base de datos");
        return productoRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    public ProductoDTO buscarPorId(Integer id) {
        log.info("Buscando producto por ID: {}", id);
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se encontro el producto por ID: {}", id);
                    return new RuntimeException("Producto no encontrado");
                });

        return convertirADTO(producto);
    }

    public Producto guardarProducto(ProductoDTO productoDTO) {
        log.info("Guardando un nuevo producto: {}", productoDTO.getNombre());

        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre(productoDTO.getNombre());
        nuevoProducto.setPrecio(productoDTO.getPrecio());
        nuevoProducto.setDescripcion(productoDTO.getDescripcion());

        Categoria categoriaEncontrada = categoriaRepository.findById(productoDTO.getIdCategoria())
                .orElseThrow(() -> {
                    log.error("ERROR: Al guardar el producto, la categoria por ID {} no existe", productoDTO.getIdCategoria());
                    return new RuntimeException("Error: La categoria no existe");
                });

        Marca marcaEncontrada = marcaRepository.findById(productoDTO.getIdMarca())
                .orElseThrow(() -> {
                    log.error("ERROR: Al guardar el producto, la marca por ID {} no existe ", productoDTO.getIdMarca());
                    return new RuntimeException("Error: La marca no existe");
                });

        nuevoProducto.setCategoria(categoriaEncontrada);
        nuevoProducto.setMarca(marcaEncontrada);

        log.info("Producto ensamblado correctamente. Guardando en la base de datos...");
        return productoRepository.save(nuevoProducto);
    }

    public Producto actualizarProducto(Integer id, Producto productoActualizado) {
        log.info("Iniciando actualizacion del producto por ID: {}", id);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERROR: No se puede actualizar. El producto por ID {} no existe", id);
                    return new RuntimeException("Producto no encontrado");
                });

        if (productoActualizado.getNombre() != null) {
            producto.setNombre(productoActualizado.getNombre());
        }

        if (productoActualizado.getDescripcion() != null) {
            producto.setDescripcion(productoActualizado.getDescripcion());
        }

        if (productoActualizado.getPrecio() != null) {
            producto.setPrecio(productoActualizado.getPrecio());
        }

        log.info("Actualizacion exitosa para el producto por ID: {}", id);
        return productoRepository.save(producto);
    }

    public String eliminar(Integer id) {
        log.info("Intentando eliminar el producto por ID: {}", id);
        try {
            Producto producto = productoRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("ERROR: No se puede eliminar. El producto por ID {} no existe", id);
                        return new RuntimeException("Producto no encontrado");
                    });

            productoRepository.delete(producto);
            log.info("Producto por ID: {} eliminado correctamente", id);
            return "Producto eliminado correctamente";

        } catch (RuntimeException e) {
            log.error("Excepcion capturada al intentar eliminar: {}", e.getMessage());
            return e.getMessage();
        }
    }

    private ProductoDTO convertirADTO(Producto producto) {

        ProductoDTO dto = new ProductoDTO();

        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setPrecio(producto.getPrecio());
        dto.setDescripcion(producto.getDescripcion());

        if (producto.getCategoria() != null) {
            dto.setIdCategoria(producto.getCategoria().getId());
            dto.setNombreCategoria(producto.getCategoria().getNombre());
        } else {
            dto.setNombreCategoria("Sin categoria");
        }

        if (producto.getMarca() != null) {
            dto.setIdMarca(producto.getMarca().getId());
            dto.setNombreMarca(producto.getMarca().getNombre());
        } else {
            dto.setNombreMarca("Sin marca");
        }

        return dto;
    }
}
