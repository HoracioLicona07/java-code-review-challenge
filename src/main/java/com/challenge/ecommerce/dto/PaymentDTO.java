package com.challenge.ecommerce.dto;

public class PaymentDTO {

    public static class PaymentRequest {
        private Long orderId;
        // PROBLEMA Junior: double para monto de pago — debería ser BigDecimal
        private double amount;
        // PROBLEMA Middle: sin validación de valores permitidos para paymentMethod
        private String paymentMethod;

        public Long getOrderId() { return orderId; }
        public void setOrderId(Long orderId) { this.orderId = orderId; }

        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }

        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    }

    public static class PaymentResponse {
        private Long id;
        private Long orderId;
        private double amount;
        private String status;
        private String paymentMethod;

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
    }
}
