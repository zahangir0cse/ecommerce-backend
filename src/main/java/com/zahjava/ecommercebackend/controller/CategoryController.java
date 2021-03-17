package com.zahjava.ecommercebackend.controller;

import com.zahjava.ecommercebackend.annotation.ApiController;
import com.zahjava.ecommercebackend.annotation.IsAdmin;
import com.zahjava.ecommercebackend.annotation.ValidateData;
import com.zahjava.ecommercebackend.dto.categoryDto.CategoryDto;
import com.zahjava.ecommercebackend.service.CategoryService;
import com.zahjava.ecommercebackend.utils.UrlConstraint;
import com.zahjava.ecommercebackend.view.Response;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@ApiController
@RequestMapping(UrlConstraint.CategoryManagement.ROOT)
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @IsAdmin
    @ValidateData
    @PostMapping(UrlConstraint.CategoryManagement.CREATE)
    public Response createCategory(@Valid @RequestBody CategoryDto categoryDto, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        return categoryService.createCategory(categoryDto);
    }

    @PutMapping(UrlConstraint.CategoryManagement.UPDATE)
    public Response updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDto categoryDto, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        return categoryService.updateCategory(id, categoryDto);
    }


    @IsAdmin
    @GetMapping(UrlConstraint.CategoryManagement.GET_ALL)
    public Response getAllCategory() {
        return categoryService.getAllCategory();
    }

    @IsAdmin
    @GetMapping(UrlConstraint.CategoryManagement.PRODUCTS_BY_CATEGORY_ID)
    public Response getAllProductByCategoryId(@PathVariable Long id) {
        return categoryService.getProductsByCategoryId(id);
    }


}
