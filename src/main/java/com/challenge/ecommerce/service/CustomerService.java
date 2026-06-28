package com.challenge.ecommerce.service;

import com.challenge.ecommerce.dto.CustomerDTO;
import com.challenge.ecommerce.entity.Customer;
import com.challenge.ecommerce.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// PROBLEMA Middle: sin mapper — convierte manual y repite código
// PROBLEMA Junior: inyección por campo
@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<CustomerDTO.CustomerResponse> getAllCustomers() {
        // PROBLEMA Middle: sin paginación
        return customerRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // PROBLEMA Junior: retorna null en lugar de lanzar excepción
    public CustomerDTO.CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer == null) return null;
        return toResponse(customer);
    }

    public CustomerDTO.CustomerResponse createCustomer(CustomerDTO.CustomerRequest request) {
        // PROBLEMA Junior: no valida si el email ya existe — permite duplicados
        // PROBLEMA Junior: no valida formato de email aquí tampoco

        // PROBLEMA Junior: comparación de String con == (no aplica aquí pero hay un ejemplo en OrderService)
        Customer customer = new Customer(request.getName(), request.getEmail(), request.getPhone());
        Customer saved = customerRepository.save(customer);
        return toResponse(saved);
    }

    // PROBLEMA Middle: conversión manual repetida en lugar de usar mapper
    // PROBLEMA Middle: este mismo bloque de código está en CustomerController también
    private CustomerDTO.CustomerResponse toResponse(Customer customer) {
        CustomerDTO.CustomerResponse response = new CustomerDTO.CustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());
        response.setActive(customer.isActive());
        return response;
    }
}
