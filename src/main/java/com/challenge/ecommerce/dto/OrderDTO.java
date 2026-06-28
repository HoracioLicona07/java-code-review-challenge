package com.challenge.ecommerce.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {

    public static class CreateOrderRequest {
        // PROBLEMA Junior: sin validación @NotNull
        private Long customerId;

        public Long getCustomerId() { return customerId; }
        public void setCustomerId(Long customerId) { this.customerId = customerId; }
    }

    public static class AddItemRequest {
        private Long productId;

        // PROBLEMA Junior: sin @Min(1) — se puede agregar cantidad 0 o negativa
        private int quantity;

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    // PROBLEMA Middle: OrderResponse mezcla datos de Customer con los de la orden
    // en lugar de tener un CustomerSummary anidado
    public static class OrderResponse {
        private Long id;
        private Long customerId;
        // PROBLEMA Middle: expone el nombre del cliente directamente — acoplamiento
        private String customerName;
        private String status;
        private double subtotal;
        private double discount;
        private double total;
        private List<OrderItemResponse> items;
        private LocalDateTime createdAt;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getCustomerId() { return customerId; }
        public void setCustomerId(Long customerId) { this.customerId = customerId; }

        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public double getSubtotal() { return subtotal; }
        public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

        public double getDiscount() { return discount; }
        public void setDiscount(double discount) { this.discount = discount; }

        public double getTotal() { return total; }
        public void setTotal(double total) { this.total = total; }

        public List<OrderItemResponse> getItems() { return items; }
        public void setItems(List<OrderItemResponse> items) { this.items = items; }

        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }

    public static class OrderItemResponse {
        private Long id;
        private Long productId;
        private String productName;
        private int quantity;
        private double unitPrice;
        private double subtotal;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public double getUnitPrice() { return unitPrice; }
        public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

        public double getSubtotal() { return subtotal; }
        public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    }
}
