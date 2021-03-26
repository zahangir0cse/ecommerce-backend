package com.zahjava.ecommercebackend.repository;

import com.zahjava.ecommercebackend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<List<Order>> findAllByIsActiveTrue();

    Optional<Order> findByIdAndIsActiveTrue(Long orderId);
}
