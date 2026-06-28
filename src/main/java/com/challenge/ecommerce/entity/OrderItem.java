package com.challenge.ecommerce.entity;

import jakarta.persistence.*;

// PROBLEMA Junior: uso de double para dinero (unitPrice, subtotal)
// PROBLEMA Senior: modelo anémico — no tiene comportamiento de dominio
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private PurchaseOrder order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    // PROBLEMA Junior: no hay validación de que quantity > 0
    private int quantity;

    // PROBLEMA Junior: double para dinero
    private double unitPrice;

    // PROBLEMA Junior: subtotal no se calcula automáticamente
    private double subtotal;

    public OrderItem() {}

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        // PROBLEMA Junior: toma el precio directamente del producto con double
        this.unitPrice = product.getPrice();
        // PROBLEMA Junior: debería calcularse con BigDecimal
        this.subtotal = this.unitPrice * this.quantity;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PurchaseOrder getOrder() { return order; }
    public void setOrder(PurchaseOrder order) { this.order = order; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
