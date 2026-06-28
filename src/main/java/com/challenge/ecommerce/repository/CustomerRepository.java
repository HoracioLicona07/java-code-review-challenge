package com.challenge.ecommerce.repository;

import com.challenge.ecommerce.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    // PROBLEMA Middle: sin paginación
    List<Customer> findByActiveTrue();
}
