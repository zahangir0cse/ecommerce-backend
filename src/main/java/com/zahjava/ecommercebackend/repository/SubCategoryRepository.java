package com.zahjava.ecommercebackend.repository;

import com.zahjava.ecommercebackend.model.categoryModel.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    Optional<SubCategory> findByNameAndIsActiveTrue(String subCategoryName);

    Optional<SubCategory> findByIdAndIsActiveTrue(Long subCategoryId);

    int countAllByIsActiveTrue();

    Optional<List<SubCategory>> findAllByIsActiveTrue();
}
