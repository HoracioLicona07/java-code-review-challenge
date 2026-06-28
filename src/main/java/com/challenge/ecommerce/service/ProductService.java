package com.challenge.ecommerce.service;

import com.challenge.ecommerce.dto.ProductDTO;
import com.challenge.ecommerce.entity.Product;
import com.challenge.ecommerce.mapper.ProductMapper;
import com.challenge.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// PROBLEMA Middle: sin @Transactional en operaciones de escritura
// PROBLEMA Junior: System.out.println en lugar de Logger
@Service
public class ProductService {

    // PROBLEMA Junior: inyección por campo en lugar de constructor
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    public List<ProductDTO.ProductResponse> getAllProducts() {
        System.out.println("Obteniendo todos los productos...");
        // PROBLEMA Middle: sin paginación — carga todo en memoria
        List<Product> products = productRepository.findAll();
        return productMapper.toResponseList(products);
    }

    public List<ProductDTO.ProductResponse> getActiveProducts() {
        return productMapper.toResponseList(productRepository.findByActiveTrue());
    }

    // PROBLEMA Junior: retorna null en lugar de Optional o lanzar excepción con mensaje claro
    public ProductDTO.ProductResponse getProductById(Long id) {
        System.out.println("Buscando producto con id: " + id);
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            // PROBLEMA Junior: mensaje de error genérico e impreciso
            System.out.println("Error: producto no encontrado");
            return null;
        }
        return productMapper.toResponse(product);
    }

    public ProductDTO.ProductResponse createProduct(ProductDTO.ProductRequest request) {
        System.out.println("Creando producto: " + request.getName());

        // PROBLEMA Junior: sin validaciones de negocio (precio negativo, stock negativo)
        // PROBLEMA Junior: no verifica si ya existe un producto con el mismo nombre

        Product product = productMapper.toEntity(request);
        Product saved = productRepository.save(product);
        return productMapper.toResponse(saved);
    }

    // PROBLEMA Middle: método de actualización muy largo con lógica mezclada
    // PROBLEMA Junior: no valida qué campos llegaron (sobreescribe con nulls si no se mandan)
    public ProductDTO.ProductResponse updateProduct(Long id, ProductDTO.ProductRequest request) {
        Product product = productRepository.findById(id).orElse(null);

        // PROBLEMA Junior: null check sin lanzar excepción adecuada
        if (product == null) {
            System.out.println("Producto no encontrado para actualizar");
            return null;
        }

        // PROBLEMA Junior: sobreescribe todos los campos aunque algunos vengan null
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(request.getCategory());
        // PROBLEMA Junior: LocalDateTime.now() disperso
        product.setUpdatedAt(LocalDateTime.now());

        Product saved = productRepository.save(product);
        System.out.println("Producto actualizado: " + saved.getId());
        return productMapper.toResponse(saved);
    }

    // PROBLEMA Middle: deleteProduct no verifica si el producto está en órdenes activas
    public void deleteProduct(Long id) {
        System.out.println("Eliminando producto: " + id);
        // PROBLEMA Junior: elimina físicamente sin soft-delete
        // PROBLEMA Junior: no verifica si existe antes de borrar
        productRepository.deleteById(id);
    }

    // PROBLEMA Junior: método duplicado con lógica casi igual a updateProduct
    public ProductDTO.ProductResponse updateStock(Long id, int newStock) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            System.out.println("Producto no encontrado");
            return null;
        }
        // PROBLEMA Junior: sin validación de stock negativo
        product.setStock(newStock);
        product.setUpdatedAt(LocalDateTime.now());
        return productMapper.toResponse(productRepository.save(product));
    }

    // PROBLEMA Junior: método que nunca se llama desde ningún controller (código muerto)
    @Deprecated
    public boolean checkAvailability(Long productId, int qty) {
        // Este método está aquí pero no lo usa nadie
        Product p = productRepository.findById(productId).orElse(null);
        if (p != null) {
            return p.getStock() >= qty;
        }
        return false;
    }
}
