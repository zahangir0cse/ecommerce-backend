package com.zahjava.ecommercebackend.service;

import com.zahjava.ecommercebackend.dto.categoryDto.SubSubSubCategoryDto;
import com.zahjava.ecommercebackend.view.Response;

public interface SubSubSubCategoryService {
    Response createSubSubSubCategory(SubSubSubCategoryDto subSubSubCategoryDto);
    Response updateSubSubSubCategory(Long id, SubSubSubCategoryDto subSubSubCategoryDto);
    Response getProductsBySubSubSubCategoryId(Long id);
    Response getAllCategories();
}
