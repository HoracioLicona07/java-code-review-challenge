package com.challenge.ecommerce.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// PROBLEMA Junior: no tiene ninguna validación a nivel de entidad
// PROBLEMA Junior: usa double para precio (debería ser BigDecimal)
// PROBLEMA Middle: entidad expuesta directamente en controllers
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // PROBLEMA Junior: sin @Column(nullable=false), sin longitud máxima
    private String name;

    private String description;

    // PROBLEMA Junior/Senior: double para dinero — pérdida de precisión
    private double price;

    private int stock;

    // PROBLEMA Junior: String para category, debería ser un Enum
    private String category;

    private boolean active;

    // PROBLEMA Junior: LocalDateTime.now() disperso — debería ser @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Constructor vacío requerido por JPA
    public Product() {}

    public Product(String name, String description, double price, int stock, String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.active = true;
        // PROBLEMA Junior: LocalDateTime.now() hardcodeado en constructor
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters y setters manuales (verbose, sin Lombok)
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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
