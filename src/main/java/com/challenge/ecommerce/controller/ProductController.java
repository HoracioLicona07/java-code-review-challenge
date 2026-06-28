package com.challenge.ecommerce.controller;

import com.challenge.ecommerce.dto.ProductDTO;
import com.challenge.ecommerce.entity.Product;
import com.challenge.ecommerce.repository.ProductRepository;
import com.challenge.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

// PROBLEMA Middle: controller con lógica de negocio (updateStock)
// PROBLEMA Middle: controller accede directamente al repositorio (productRepository)
// PROBLEMA Junior: respuestas HTTP incorrectas
// PROBLEMA Middle: no usa @Valid en el request body
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // PROBLEMA Middle: acceso directo al repositorio desde el controller
    @Autowired
    private ProductRepository productRepository;

    // PROBLEMA Junior: retorna 200 aunque debería retornar 200 (este está bien — punto de comparación)
    @GetMapping
    public ResponseEntity<List<ProductDTO.ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO.ProductResponse> getProductById(@PathVariable Long id) {
        ProductDTO.ProductResponse response = productService.getProductById(id);
        // PROBLEMA Junior: retorna 200 con body null cuando no existe — debería ser 404
        if (response == null) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(response);
    }

    // PROBLEMA Middle: sin @Valid — no valida el request body
    @PostMapping
    public ResponseEntity<ProductDTO.ProductResponse> createProduct(
            @RequestBody ProductDTO.ProductRequest request) {
        // PROBLEMA Junior: debería retornar 201 Created, no 200 OK
        return ResponseEntity.ok(productService.createProduct(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO.ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDTO.ProductRequest request) {
        ProductDTO.ProductResponse response = productService.updateProduct(id, request);
        // PROBLEMA Junior: retorna 200 con null si no existe — debería ser 404
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        // PROBLEMA Junior: debería retornar 204 No Content, no 200
        return ResponseEntity.ok().build();
    }

    // PROBLEMA Middle: lógica de negocio en el controller
    // PROBLEMA Middle: acceso directo al repositorio desde el controller
    @PatchMapping("/{id}/stock")
    public ResponseEntity<?> updateStock(@PathVariable Long id, @RequestParam int quantity) {
        // PROBLEMA Junior: sin validación de quantity negativo
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            // PROBLEMA Junior: retorna 200 con mensaje de error en lugar de 404
            return ResponseEntity.ok("Producto no encontrado");
        }

        // PROBLEMA Junior: magic number — sin validación de stock mínimo
        if (quantity < 0 && product.getStock() + quantity < 0) {
            return ResponseEntity.badRequest().body("Stock no puede ser negativo");
        }

        // PROBLEMA Middle: lógica de negocio aquí en lugar del servicio
        product.setStock(product.getStock() + quantity);
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);

        return ResponseEntity.ok(product); // PROBLEMA Middle: expone entidad directamente
    }

    // PROBLEMA Middle: endpoint que retorna la entidad directamente, no el DTO
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable String category) {
        // PROBLEMA Middle: expone entidad JPA directamente
        // PROBLEMA Middle: acceso directo a repositorio desde controller
        List<Product> products = productRepository.findByCategory(category);
        return ResponseEntity.ok(products);
    }
}
