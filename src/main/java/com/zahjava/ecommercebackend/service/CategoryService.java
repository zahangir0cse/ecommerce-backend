package com.zahjava.ecommercebackend.service;


import com.zahjava.ecommercebackend.dto.categoryDto.CategoryDto;
import com.zahjava.ecommercebackend.view.Response;

public interface CategoryService {
    Response createCategory(CategoryDto categoryDto);

    Response updateCategory(Long categoryId,CategoryDto categoryDto);

    Response getAllCategory();

    Response getProductsByCategoryId(Long categoryId);
}
