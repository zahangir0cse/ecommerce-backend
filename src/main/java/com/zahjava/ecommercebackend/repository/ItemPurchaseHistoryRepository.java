package com.zahjava.ecommercebackend.repository;

import com.zahjava.ecommercebackend.model.ItemPurchaseHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemPurchaseHistoryRepository extends JpaRepository<ItemPurchaseHistory, Long> {
    Optional<ItemPurchaseHistory> findByProductIdAndIsActiveTrue(Long productId);
}
