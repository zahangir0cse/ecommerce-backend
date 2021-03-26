package com.zahjava.ecommercebackend.service;

import com.zahjava.ecommercebackend.dto.CategoryCommonDto;
import com.zahjava.ecommercebackend.view.Response;

public interface CategoryControlService {
    Response createCategoryNew(CategoryCommonDto categoryCommonDto);

    Response getCategory(Long id);

    Response getAllRootCategory();

}
