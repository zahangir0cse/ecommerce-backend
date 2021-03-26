package com.zahjava.ecommercebackend.controller;

import com.zahjava.ecommercebackend.annotation.ApiController;
import com.zahjava.ecommercebackend.annotation.IsAdmin;
import com.zahjava.ecommercebackend.annotation.ValidateData;
import com.zahjava.ecommercebackend.dto.CategoryCommonDto;
import com.zahjava.ecommercebackend.service.CategoryControlService;
import com.zahjava.ecommercebackend.utils.UrlConstraint;
import com.zahjava.ecommercebackend.view.Response;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@ApiController
@RequestMapping(UrlConstraint.CategoryManagement.ROOT)
public class CategoryController {
    private final CategoryControlService categoryControlService;

    public CategoryController(CategoryControlService categoryControlService) {
        this.categoryControlService = categoryControlService;
    }

    @IsAdmin
    @ValidateData
    @PostMapping(UrlConstraint.CategoryManagement.CREATE)
    public Response createCategory(@Valid @RequestBody CategoryCommonDto categoryCommonDto, HttpServletRequest request, HttpServletResponse httpServletResponse) {
        return categoryControlService.createCategoryNew(categoryCommonDto);
    }

    @IsAdmin
    @GetMapping(UrlConstraint.CategoryManagement.GET)
    public Response getCategoryById(@PathVariable Long id) {
        return categoryControlService.getCategory(id);
    }

    @IsAdmin
    @GetMapping(UrlConstraint.CategoryManagement.GET_ALL_ROOT_CATEGORY)
    public Response getAllRootCategory() {
        return categoryControlService.getAllRootCategory();
    }


}
