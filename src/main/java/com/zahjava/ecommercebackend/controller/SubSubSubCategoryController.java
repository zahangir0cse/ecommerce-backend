package com.zahjava.ecommercebackend.controller;

import com.zahjava.ecommercebackend.annotation.ApiController;
import com.zahjava.ecommercebackend.annotation.IsAdmin;
import com.zahjava.ecommercebackend.annotation.ValidateData;
import com.zahjava.ecommercebackend.dto.categoryDto.SubSubSubCategoryDto;
import com.zahjava.ecommercebackend.service.SubSubSubCategoryService;
import com.zahjava.ecommercebackend.utils.UrlConstraint;
import com.zahjava.ecommercebackend.view.Response;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@ApiController
@RequestMapping(UrlConstraint.SubSubSubCategoryManagement.ROOT)
public class SubSubSubCategoryController {
    private final SubSubSubCategoryService subSubSubCategoryService;

    public SubSubSubCategoryController(SubSubSubCategoryService subSubSubCategoryService) {
        this.subSubSubCategoryService = subSubSubCategoryService;
    }

    @ValidateData
    @PostMapping(UrlConstraint.SubSubSubCategoryManagement.CREATE)
    public Response createSubSubSubCategory(@Valid @RequestBody SubSubSubCategoryDto subSubSubCategoryDto, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        return subSubSubCategoryService.createSubSubSubCategory(subSubSubCategoryDto);
    }

    @IsAdmin
    @ValidateData
    @PutMapping(UrlConstraint.SubSubSubCategoryManagement.UPDATE)
    public Response updateSubSubSubCategory(@PathVariable Long id, @Valid @RequestBody SubSubSubCategoryDto subSubSubCategoryDto, HttpServletRequest request, HttpServletResponse response) {
        return subSubSubCategoryService.updateSubSubSubCategory(id, subSubSubCategoryDto);
    }

    @IsAdmin
    @ValidateData
    @GetMapping(UrlConstraint.SubSubSubCategoryManagement.GET_ALL)
    public Response getAllCategories() {
        return subSubSubCategoryService.getAllCategories();
    }

    @IsAdmin
    @ValidateData
    @GetMapping(UrlConstraint.SubSubSubCategoryManagement.PRODUCTS_BY_SUBSUBSUBCATEGOY_ID)
    public Response getAllProductsBySubSubSubCategoryId(@PathVariable Long id, HttpServletRequest httpServletRequest, HttpServletResponse response) {
        return subSubSubCategoryService.getProductsBySubSubSubCategoryId(id);
    }


}
