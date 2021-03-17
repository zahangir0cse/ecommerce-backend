package com.zahjava.ecommercebackend.service;

import com.zahjava.ecommercebackend.dto.categoryDto.SubSubCategoryDto;
import com.zahjava.ecommercebackend.view.Response;

public interface SubSubCategoryService {
    Response createSubSubCategory(SubSubCategoryDto subSubCategoryDto);

    Response updateSubSubCategory(Long id, SubSubCategoryDto subSubCategoryDto);

    Response getProductsBySubSubCategoryId(Long subSubCategoryId);

    Response getAllSubSubCategories();
}
