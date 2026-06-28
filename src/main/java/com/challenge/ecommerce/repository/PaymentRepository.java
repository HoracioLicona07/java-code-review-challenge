package com.challenge.ecommerce.repository;

import com.challenge.ecommerce.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // PROBLEMA Senior: busca por orderId (Long) en lugar de por relación con PurchaseOrder
    Optional<Payment> findByOrderId(Long orderId);

    List<Payment> findByStatus(String status);
}
