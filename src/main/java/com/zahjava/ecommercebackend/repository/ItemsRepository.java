package com.zahjava.ecommercebackend.repository;

import com.zahjava.ecommercebackend.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemsRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByNameAndIsProductFalseAndIsActiveTrue(String name);

    Optional<Item> findByNameAndIsProductTrueAndIsActiveTrue(String name);

    Optional<Item> findByIdAndIsProductFalseAndIsActiveTrue(Long id);

    Optional<List<Item>> findAllByIsActiveTrueAndIsProductTrue();

    Optional<Item> findByIdAndIsActiveTrueAndIsProductTrue(Long id);

}
