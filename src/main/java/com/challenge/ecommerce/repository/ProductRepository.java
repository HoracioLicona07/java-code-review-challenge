package com.challenge.ecommerce.repository;

import com.challenge.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// PROBLEMA Middle: sin paginación — findAll() cargará todos los productos a memoria
// PROBLEMA Senior: sin índices definidos en la entidad para búsquedas frecuentes
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // PROBLEMA Middle: carga todos los activos sin paginación
    List<Product> findByActiveTrue();

    // PROBLEMA Junior: búsqueda case-sensitive sin que el alumno lo note
    List<Product> findByCategory(String category);

    // Existe pero nadie la usa — código muerto potencial
    List<Product> findByNameContaining(String keyword);
}
