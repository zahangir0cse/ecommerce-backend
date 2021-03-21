package com.zahjava.ecommercebackend.repository;

import com.zahjava.ecommercebackend.model.ItemRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRelationHelperRepository extends JpaRepository<ItemRelation,Long> {
}
