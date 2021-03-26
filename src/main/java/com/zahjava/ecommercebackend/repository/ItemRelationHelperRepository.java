package com.zahjava.ecommercebackend.repository;

import com.zahjava.ecommercebackend.model.ItemRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRelationHelperRepository extends JpaRepository<ItemRelation, Long> {
    List<ItemRelation> findAllByItemParentIdAndIsActiveTrue(Long parentId);
    Optional<ItemRelation> findByItemParentIdAndIsActiveTrue(Long itemParentId);
}
