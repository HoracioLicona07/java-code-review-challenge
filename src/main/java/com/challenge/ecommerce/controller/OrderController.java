package com.challenge.ecommerce.controller;

import com.challenge.ecommerce.dto.OrderDTO;
import com.challenge.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDTO.OrderResponse>> getAllOrders() {
        // PROBLEMA Middle: sin paginación
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        OrderDTO.OrderResponse response = orderService.getOrderById(id);
        // PROBLEMA Junior: retorna 200 con null en lugar de 404
        if (response == null) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDTO.OrderResponse>> getOrdersByCustomer(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getOrdersByCustomer(customerId));
    }

    // PROBLEMA Middle: sin @Valid
    @PostMapping
    public ResponseEntity<OrderDTO.OrderResponse> createOrder(
            @RequestBody OrderDTO.CreateOrderRequest request) {
        // PROBLEMA Junior: debería retornar 201 Created
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<OrderDTO.OrderResponse> addItem(
            @PathVariable Long id,
            @RequestBody OrderDTO.AddItemRequest request) {
        return ResponseEntity.ok(orderService.addItemToOrder(id, request));
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<OrderDTO.OrderResponse> confirmOrder(@PathVariable Long id) {
        // PROBLEMA Junior: si lanza RuntimeException, Spring retorna 500 en lugar de 400/409
        // PROBLEMA Middle: falta manejo global de excepciones
        return ResponseEntity.ok(orderService.confirmOrder(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderDTO.OrderResponse> cancelOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }
}
