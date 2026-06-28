package com.challenge.ecommerce.repository;

import com.challenge.ecommerce.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<PurchaseOrder, Long> {

    // PROBLEMA Middle: N+1 potencial — no usa JOIN FETCH para customer e items
    List<PurchaseOrder> findByCustomerId(Long customerId);

    // PROBLEMA Middle: sin paginación
    List<PurchaseOrder> findByStatus(String status);

    // Query con JOIN FETCH (esta sí está bien — punto de comparación para alumnos)
    @Query("SELECT o FROM PurchaseOrder o JOIN FETCH o.customer WHERE o.id = :id")
    java.util.Optional<PurchaseOrder> findByIdWithCustomer(Long id);
}
