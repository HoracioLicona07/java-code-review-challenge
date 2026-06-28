package com.challenge.ecommerce.service;

import com.challenge.ecommerce.dto.ProductDTO;
import com.challenge.ecommerce.entity.Product;
import com.challenge.ecommerce.mapper.ProductMapper;
import com.challenge.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product sampleProduct;
    private ProductDTO.ProductResponse sampleResponse;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product("Laptop", "Laptop de prueba", 9999.99, 10, "ELECTRONICS");
        sampleProduct.setId(1L);

        sampleResponse = new ProductDTO.ProductResponse();
        sampleResponse.setId(1L);
        sampleResponse.setName("Laptop");
        sampleResponse.setPrice(9999.99);
    }

    // PROBLEMA Junior: test que solo verifica que el objeto no sea null — sin aserciones reales
    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(sampleProduct));
        when(productMapper.toResponseList(any())).thenReturn(List.of(sampleResponse));

        List<ProductDTO.ProductResponse> result = productService.getAllProducts();

        // PROBLEMA Junior: solo verifica que no sea null — no verifica contenido
        assertNotNull(result);
        // Falta: assertEquals(1, result.size());
        // Falta: assertEquals("Laptop", result.get(0).getName());
    }

    @Test
    void testGetProductById_whenExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productMapper.toResponse(sampleProduct)).thenReturn(sampleResponse);

        ProductDTO.ProductResponse result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        // PROBLEMA Junior: no verifica que se llamó al mapper exactamente una vez
        // verify(productMapper, times(1)).toResponse(sampleProduct);
    }

    // PROBLEMA Junior: test que verifica que retorna null cuando no existe
    // Debería verificar que lanza una excepción adecuada
    @Test
    void testGetProductById_whenNotExists() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        ProductDTO.ProductResponse result = productService.getProductById(99L);

        // PROBLEMA Junior: acepta null como respuesta válida — no documenta el comportamiento correcto
        assertNull(result);
        // Lo correcto sería: assertThrows(ProductNotFoundException.class, ...)
    }

    @Test
    void testCreateProduct() {
        ProductDTO.ProductRequest request = new ProductDTO.ProductRequest();
        request.setName("Mouse");
        request.setPrice(500.0);
        request.setStock(20);
        request.setCategory("ELECTRONICS");

        when(productMapper.toEntity(any())).thenReturn(sampleProduct);
        when(productRepository.save(any())).thenReturn(sampleProduct);
        when(productMapper.toResponse(any())).thenReturn(sampleResponse);

        ProductDTO.ProductResponse result = productService.createProduct(request);

        // PROBLEMA Junior: no verifica que se guardó en el repositorio
        assertNotNull(result);
        // Falta: verify(productRepository).save(any(Product.class));
    }

    // PROBLEMA Junior: test que prueba deleteProduct sin verificar que se llamó deleteById
    @Test
    void testDeleteProduct() {
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        // PROBLEMA Junior: test vacío — no verifica nada
        // Falta: verify(productRepository).deleteById(1L);
    }

    // Test bien escrito — punto de comparación para alumnos
    @Test
    void testUpdateStock_whenProductExists_shouldUpdateSuccessfully() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any())).thenReturn(sampleProduct);
        when(productMapper.toResponse(any())).thenReturn(sampleResponse);

        ProductDTO.ProductResponse result = productService.updateStock(1L, 5);

        assertNotNull(result);
        verify(productRepository).findById(1L);
        verify(productRepository).save(sampleProduct);
    }
}
