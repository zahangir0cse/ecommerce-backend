package com.zahjava.ecommercebackend.repository;

import com.zahjava.ecommercebackend.model.categoryModel.SubSubSubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubSubSubCategoryRepository extends JpaRepository<SubSubSubCategory, Long> {
    Optional<SubSubSubCategory> findByNameAndIsActiveTrue(String name);

    Optional<SubSubSubCategory> findByIdAndIsActiveTrue(Long id);

    Optional<List<SubSubSubCategory>> findAllByIsActiveTrue();

    int countAllByIsActiveTrue();

}
