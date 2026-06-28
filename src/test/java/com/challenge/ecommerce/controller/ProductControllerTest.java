package com.challenge.ecommerce.controller;

import com.challenge.ecommerce.dto.ProductDTO;
import com.challenge.ecommerce.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// PROBLEMA Middle: test de controller sin verificar los status codes correctos
// PROBLEMA Junior: faltan casos de prueba para 404, 400, etc.
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    // PROBLEMA Middle: ProductController también inyecta ProductRepository directamente
    // y @WebMvcTest no lo levanta — esto fará fallar el test
    // Solución: agregar @MockBean para ProductRepository también
    @MockBean
    private com.challenge.ecommerce.repository.ProductRepository productRepository;

    @Test
    void testGetAllProducts_returns200() throws Exception {
        ProductDTO.ProductResponse resp = new ProductDTO.ProductResponse();
        resp.setId(1L);
        resp.setName("Laptop");

        when(productService.getAllProducts()).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop"));
    }

    // PROBLEMA Junior: este test verifica que /api/products/{id} retorna 200
    // PERO en la implementación real retorna 200 aunque el producto no exista (con body null)
    // El test pasa pero el comportamiento es incorrecto
    @Test
    void testGetProductById_whenNotExists_shouldReturn404() throws Exception {
        when(productService.getProductById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/products/99"))
                // PROBLEMA Junior: espera 404 pero el controller retorna 200 con null
                // Este test FALLA — documenta el bug
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProduct_returns200() throws Exception {
        ProductDTO.ProductRequest request = new ProductDTO.ProductRequest();
        request.setName("Mouse");
        request.setPrice(500.0);
        request.setStock(10);
        request.setCategory("ELECTRONICS");

        ProductDTO.ProductResponse response = new ProductDTO.ProductResponse();
        response.setId(1L);
        response.setName("Mouse");

        when(productService.createProduct(any())).thenReturn(response);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                // PROBLEMA Junior: debería ser 201 CREATED, no 200 OK
                // El test pasa pero el contrato REST es incorrecto
                .andExpect(status().isOk());
    }
}
