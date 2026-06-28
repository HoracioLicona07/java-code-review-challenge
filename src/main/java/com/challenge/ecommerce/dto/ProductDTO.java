package com.challenge.ecommerce.dto;

// PROBLEMA Junior: falta validación de email en CustomerRequest
// PROBLEMA Junior: falta @NotBlank, @Positive, etc. en algunos campos

public class ProductDTO {

    // DTO para crear/actualizar producto
    public static class ProductRequest {
        // PROBLEMA Junior: String name sin @NotBlank
        private String name;
        private String description;

        // PROBLEMA Junior: double para precio — debería ser BigDecimal
        private double price;

        // PROBLEMA Junior: sin validación @Min(0)
        private int stock;

        private String category;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }

        public int getStock() { return stock; }
        public void setStock(int stock) { this.stock = stock; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
    }

    // DTO de respuesta — expone todos los campos incluyendo updatedAt
    // PROBLEMA Middle: mapper incompleto no siempre llena todos los campos
    public static class ProductResponse {
        private Long id;
        private String name;
        private String description;
        private double price;
        private int stock;
        private String category;
        private boolean active;
        // PROBLEMA Junior: falta createdAt en la respuesta (mapper incompleto)

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }

        public int getStock() { return stock; }
        public void setStock(int stock) { this.stock = stock; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
    }
}
