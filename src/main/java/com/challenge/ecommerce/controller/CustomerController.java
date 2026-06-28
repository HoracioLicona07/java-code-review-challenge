package com.challenge.ecommerce.controller;

import com.challenge.ecommerce.dto.CustomerDTO;
import com.challenge.ecommerce.entity.Customer;
import com.challenge.ecommerce.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// PROBLEMA Middle: duplica lógica de conversión de CustomerService
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerDTO.CustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
        CustomerDTO.CustomerResponse response = customerService.getCustomerById(id);
        // PROBLEMA Junior: retorna 200 OK con null en lugar de 404
        if (response == null) {
            return ResponseEntity.ok("Cliente no encontrado");
        }
        return ResponseEntity.ok(response);
    }

    // PROBLEMA Middle: sin @Valid
    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody CustomerDTO.CustomerRequest request) {
        // PROBLEMA Junior: validación manual en el controller (duplicada)
        if (request.getName() == null || request.getName().isEmpty()) {
            return ResponseEntity.badRequest().body("El nombre es requerido");
        }
        // PROBLEMA Junior: sin validación de email (cualquier string es válido)
        // PROBLEMA Junior: debería retornar 201 Created
        CustomerDTO.CustomerResponse response = customerService.createCustomer(request);
        return ResponseEntity.ok(response);
    }

    // PROBLEMA Middle: endpoint que expone la entidad directamente (sin DTO)
    // Este endpoint existe para "facilitar debugging" pero expone internals
    @GetMapping("/{id}/raw")
    public ResponseEntity<Customer> getCustomerRaw(@PathVariable Long id) {
        // PROBLEMA Middle: expone entidad JPA con lazy collections potencialmente
        // PROBLEMA Senior: endpoint de debugging que llegó a producción
        return ResponseEntity.ok(null); // siempre retorna null — código muerto/roto
    }
}
