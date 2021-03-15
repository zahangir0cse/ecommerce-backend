package com.zahjava.ecommercebackend.repository;

import com.zahjava.ecommercebackend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByNameAndIsActiveTrue(String productName);

    Optional<Product> findByIdAndIsActiveTrue(Long productId);
}
