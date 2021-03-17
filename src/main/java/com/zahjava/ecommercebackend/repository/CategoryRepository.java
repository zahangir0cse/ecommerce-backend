package com.zahjava.ecommercebackend.repository;

import com.zahjava.ecommercebackend.model.categoryModel.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByNameAndIsActiveTrue(String categoryName);

    Optional<Category> findByIdAndIsActiveTrue(Long categoryId);

    int countAllByIsActiveTrue();

    Optional<List<Category>> findAllByIsActiveTrue();
}
