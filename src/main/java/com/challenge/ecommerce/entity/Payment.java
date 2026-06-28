package com.challenge.ecommerce.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// PROBLEMA Junior: double para amount
// PROBLEMA Middle: paymentMethod como String sin control de valores
// PROBLEMA Senior: no hay relación JPA con PurchaseOrder — solo orderId como Long
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // PROBLEMA Senior: relación perdida — debería ser @ManyToOne con PurchaseOrder
    private Long orderId;

    // PROBLEMA Junior: double para dinero
    private double amount;

    // PROBLEMA Middle: sin validación de valores posibles
    private String status;

    private String paymentMethod;

    private LocalDateTime createdAt;

    public Payment() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
