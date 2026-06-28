package com.challenge.ecommerce.service;

import com.challenge.ecommerce.dto.PaymentDTO;
import com.challenge.ecommerce.entity.Payment;
import com.challenge.ecommerce.entity.PurchaseOrder;
import com.challenge.ecommerce.repository.OrderRepository;
import com.challenge.ecommerce.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// PROBLEMA Senior: no hay estrategia de extensión para métodos de pago
// PROBLEMA Junior: usa double para montos de dinero
// PROBLEMA Middle: sin @Transactional
// PROBLEMA Senior: no hay idempotencia — se puede procesar el mismo pago dos veces
@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    // PROBLEMA Senior: método que hace todo: valida, procesa, actualiza, notifica
    // PROBLEMA Junior: no valida que el monto coincida con el total de la orden
    // PROBLEMA Senior: sin retry en caso de fallo del procesador externo simulado
    public PaymentDTO.PaymentResponse processPayment(PaymentDTO.PaymentRequest request) {
        System.out.println("Procesando pago para orden: " + request.getOrderId());

        // PROBLEMA Senior: no hay verificación de idempotencia
        // Si se llama dos veces con el mismo orderId, se crean dos pagos
        PurchaseOrder order = orderRepository.findById(request.getOrderId()).orElse(null);
        if (order == null) {
            throw new RuntimeException("Orden no encontrada para pago");
        }

        // PROBLEMA Junior: comparación con == en lugar de .equals()
        if (order.getStatus() != "CONFIRMED") {
            throw new RuntimeException("La orden debe estar confirmada para procesar pago");
        }

        // PROBLEMA Junior: double para dinero — precisión perdida
        // PROBLEMA Junior: no valida que amount > 0
        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());

        // PROBLEMA Senior: procesador de pago simulado sin estrategia extensible
        // PROBLEMA Senior: sin manejo de fallos del procesador externo
        boolean paymentApproved = simulatePaymentProcessor(request.getPaymentMethod(),
                request.getAmount());

        if (paymentApproved) {
            payment.setStatus("APPROVED");
            // PROBLEMA Middle: actualiza el estado de la orden dentro del PaymentService
            // violando separación de responsabilidades
            order.setStatus("PAID");
            orderRepository.save(order);
        } else {
            payment.setStatus("REJECTED");
        }

        Payment saved = paymentRepository.save(payment);
        System.out.println("Pago procesado: " + saved.getId() + " status: " + saved.getStatus());

        return toResponse(saved);
    }

    // PROBLEMA Junior: método privado mezclado con lógica hardcodeada
    // PROBLEMA Senior: no hay abstracción del procesador externo — imposible de testear correctamente
    private boolean simulatePaymentProcessor(String method, double amount) {
        // PROBLEMA Junior: comparaciones de String con ==
        if (method == "CASH") {
            return true;
        }
        // PROBLEMA Junior: magic number — 50000 sin constante
        if (amount > 50000) {
            System.out.println("Monto muy alto, rechazado");
            return false;
        }
        // Simula 90% de éxito para tarjetas
        return Math.random() > 0.1;
    }

    // PROBLEMA Middle: conversión manual repetida — debería estar en un PaymentMapper
    private PaymentDTO.PaymentResponse toResponse(Payment payment) {
        PaymentDTO.PaymentResponse response = new PaymentDTO.PaymentResponse();
        response.setId(payment.getId());
        response.setOrderId(payment.getOrderId());
        response.setAmount(payment.getAmount());
        response.setStatus(payment.getStatus());
        response.setPaymentMethod(payment.getPaymentMethod());
        return response;
    }
}
