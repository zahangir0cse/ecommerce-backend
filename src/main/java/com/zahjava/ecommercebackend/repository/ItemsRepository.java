package com.zahjava.ecommercebackend.repository;

import com.zahjava.ecommercebackend.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemsRepository extends JpaRepository<Item, Long> {

 Optional<Item> findByNameAndIsProductFalse(String name);

}
