package com.challenge.ecommerce.controller;

import com.challenge.ecommerce.dto.PaymentDTO;
import com.challenge.ecommerce.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // PROBLEMA Middle: sin @Valid
    // PROBLEMA Senior: sin idempotency key en el header
    @PostMapping("/process")
    public ResponseEntity<PaymentDTO.PaymentResponse> processPayment(
            @RequestBody PaymentDTO.PaymentRequest request) {
        // PROBLEMA Junior: sin validación de que orderId y amount no sean null/0
        return ResponseEntity.ok(paymentService.processPayment(request));
    }
}
