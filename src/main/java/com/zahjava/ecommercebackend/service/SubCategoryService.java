package com.zahjava.ecommercebackend.service;

import com.zahjava.ecommercebackend.dto.categoryDto.SubCategoryDto;
import com.zahjava.ecommercebackend.view.Response;

public interface SubCategoryService {
    Response createSubCategory(SubCategoryDto subCategoryDto);

    Response updateSubCategory(Long id, SubCategoryDto subCategoryDto);

    Response getAllSubCategory();

    Response getProductsBySubCategoryId(Long subCategoryId);
}
