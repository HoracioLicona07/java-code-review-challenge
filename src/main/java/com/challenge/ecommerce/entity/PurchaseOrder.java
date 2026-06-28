package com.challenge.ecommerce.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// PROBLEMA Middle/Senior: status como String en lugar de Enum controlado
// PROBLEMA Senior: no hay control de transiciones de estado
// PROBLEMA Senior: modelo anémico — el objeto no valida sus propias transiciones
@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();

    // PROBLEMA Middle: status como String — nadie controla qué valores son válidos
    // Valores posibles: "PENDING", "CONFIRMED", "CANCELLED", "SHIPPED"
    private String status;

    // PROBLEMA Junior: double para dinero
    private double subtotal;

    // PROBLEMA Junior: magic number para descuento en el servicio
    private double discount;

    private double total;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public PurchaseOrder() {
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public PurchaseOrder(Customer customer) {
        this();
        this.customer = customer;
    }

    // PROBLEMA Senior: no hay validación de transición — se puede llamar con cualquier String
    public void setStatus(String status) {
        this.status = status;
    }

    // PROBLEMA Junior: recalcular total mezcla lógica de negocio dentro de la entidad
    // sin consistencia — a veces se llama, a veces no
    public void recalculateTotal() {
        this.subtotal = items.stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
        this.total = this.subtotal - this.discount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public String getStatus() { return status; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
