package com.zahjava.ecommercebackend.controller;

import com.zahjava.ecommercebackend.annotation.ApiController;
import com.zahjava.ecommercebackend.annotation.IsAdmin;
import com.zahjava.ecommercebackend.annotation.ValidateData;
import com.zahjava.ecommercebackend.dto.categoryDto.SubCategoryDto;
import com.zahjava.ecommercebackend.service.SubCategoryService;
import com.zahjava.ecommercebackend.utils.UrlConstraint;
import com.zahjava.ecommercebackend.view.Response;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@ApiController
@RequestMapping(UrlConstraint.SubCategoryManagement.ROOT)
public class SubCategoryController {
    private final SubCategoryService subCategoryService;

    public SubCategoryController(SubCategoryService subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    @IsAdmin
    @ValidateData
    @PostMapping(UrlConstraint.SubCategoryManagement.CREATE)
    public Response createSubCategory(@Valid @RequestBody SubCategoryDto subCategoryDto, HttpServletRequest request, HttpServletResponse response) {
        return subCategoryService.createSubCategory(subCategoryDto);
    }


    @IsAdmin
    @PutMapping(UrlConstraint.SubCategoryManagement.UPDATE)
    public Response updateSubCategory(@PathVariable Long id, @Valid @RequestBody SubCategoryDto subCategoryDto, HttpServletRequest request, HttpServletResponse response) {
        return subCategoryService.updateSubCategory(id, subCategoryDto);
    }


    @IsAdmin
    @ValidateData
    @GetMapping(UrlConstraint.SubCategoryManagement.GET_ALL)
    public Response getAllSubCategory() {
        return subCategoryService.getAllSubCategory();
    }


    @GetMapping(UrlConstraint.SubCategoryManagement.PRODUCTS_BY_SUBCATEGOY_ID)
    public Response getProductsBySubCategoryId(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        return subCategoryService.getProductsBySubCategoryId(id);
    }

}
