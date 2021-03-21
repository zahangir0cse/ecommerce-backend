package com.zahjava.ecommercebackend.service;

import com.zahjava.ecommercebackend.dto.ProductDto;
import com.zahjava.ecommercebackend.dto.categoryDto.SubSubSubCategoryDto;
import com.zahjava.ecommercebackend.model.Product;
import com.zahjava.ecommercebackend.view.Response;

import java.util.List;

public interface SubSubSubCategoryService {
    Response createSubSubSubCategory(SubSubSubCategoryDto subSubSubCategoryDto);
    Response updateSubSubSubCategory(Long id, SubSubSubCategoryDto subSubSubCategoryDto);
    Response getProductsBySubSubSubCategoryId(Long id);
    Response getAllCategories();
    List<ProductDto> getProductList(List<Product> productList);
}
