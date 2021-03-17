package com.zahjava.ecommercebackend.repository;

import com.zahjava.ecommercebackend.model.categoryModel.SubSubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubSubCategoryRepository extends JpaRepository<SubSubCategory, Long> {
    Optional<SubSubCategory> findByNameAndIsActiveTrue(String name);

    Optional<SubSubCategory> findByIdAndIsActiveTrue(Long id);

    int countAllByIsActiveTrue();

    Optional<List<SubSubCategory>>findAllByIsActiveTrue();
}
