package com.challenge.ecommerce.mapper;

import com.challenge.ecommerce.dto.ProductDTO;
import com.challenge.ecommerce.entity.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

// PROBLEMA Middle: mapper incompleto — no mapea todos los campos
// PROBLEMA Junior: no hay mapper para algunas entidades (Customer usa la entidad directamente)
@Component
public class ProductMapper {

    // PROBLEMA Middle: createdAt y updatedAt no se mapean — se pierden en la respuesta
    public ProductDTO.ProductResponse toResponse(Product product) {
        if (product == null) return null; // al menos valida null

        ProductDTO.ProductResponse response = new ProductDTO.ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        // PROBLEMA Junior: falta mapear description
        // response.setDescription(product.getDescription()); // esto está comentado "temporalmente"
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setCategory(product.getCategory());
        response.setActive(product.isActive());
        // PROBLEMA Middle: createdAt NO se mapea — campo olvidado
        return response;
    }

    public List<ProductDTO.ProductResponse> toResponseList(List<Product> products) {
        // PROBLEMA Middle: uso innecesario de Collectors.toList() — en Java 16+ usar .toList()
        return products.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Product toEntity(ProductDTO.ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(request.getCategory());
        product.setActive(true);
        // PROBLEMA Junior: LocalDateTime.now() hardcodeado aquí también
        product.setCreatedAt(java.time.LocalDateTime.now());
        product.setUpdatedAt(java.time.LocalDateTime.now());
        return product;
    }
}
