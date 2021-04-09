package com.zahjava.ecommercebackend.repository;

import com.zahjava.ecommercebackend.model.ItemSalePriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemSalePriceHistoryRepository extends JpaRepository<ItemSalePriceHistory, Long> {
    Optional<ItemSalePriceHistory> findByProductIdAndIsActivePriceTrueAndIsActiveTrue(Long productId);
}
