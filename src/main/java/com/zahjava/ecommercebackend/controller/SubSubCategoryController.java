package com.zahjava.ecommercebackend.controller;

import com.zahjava.ecommercebackend.annotation.ApiController;
import com.zahjava.ecommercebackend.annotation.IsAdmin;
import com.zahjava.ecommercebackend.annotation.ValidateData;
import com.zahjava.ecommercebackend.dto.categoryDto.SubSubCategoryDto;
import com.zahjava.ecommercebackend.service.SubSubCategoryService;
import com.zahjava.ecommercebackend.utils.UrlConstraint;
import com.zahjava.ecommercebackend.view.Response;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@ApiController
@RequestMapping(UrlConstraint.SubSubCategoryManagement.ROOT)
public class SubSubCategoryController {
    private final SubSubCategoryService subSubCategoryService;

    public SubSubCategoryController(SubSubCategoryService subSubCategoryService) {
        this.subSubCategoryService = subSubCategoryService;
    }

    @IsAdmin
    @ValidateData
    @PostMapping(UrlConstraint.SubSubCategoryManagement.CREATE)
    public Response createSubSubCategory(@Valid @RequestBody SubSubCategoryDto subSubCategoryDto, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        return subSubCategoryService.createSubSubCategory(subSubCategoryDto);
    }

    @IsAdmin
    @ValidateData
    @PutMapping(UrlConstraint.SubSubCategoryManagement.UPDATE)
    public Response updateSubSubCategory(@PathVariable Long id, @Valid @RequestBody SubSubCategoryDto subSubCategoryDto, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        return subSubCategoryService.updateSubSubCategory(id, subSubCategoryDto);
    }

    @IsAdmin
    @ValidateData
    @GetMapping(UrlConstraint.SubSubCategoryManagement.GET_ALL)
    public Response getAllSubSubCategories() {
        return subSubCategoryService.getAllSubSubCategories();
    }

    @GetMapping(UrlConstraint.SubSubCategoryManagement.PRODUCTS_BY_SUBSUBCATEGOY_ID)
    public Response getProductsBySubSubCategoryId(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        return subSubCategoryService.getProductsBySubSubCategoryId(id);
    }
}
