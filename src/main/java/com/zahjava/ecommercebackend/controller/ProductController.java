package com.zahjava.ecommercebackend.controller;

import com.zahjava.ecommercebackend.annotation.ApiController;
import com.zahjava.ecommercebackend.annotation.IsAdmin;
import com.zahjava.ecommercebackend.annotation.ValidateData;
import com.zahjava.ecommercebackend.dto.ItemDto;
import com.zahjava.ecommercebackend.service.ProductService;
import com.zahjava.ecommercebackend.utils.UrlConstraint;
import com.zahjava.ecommercebackend.view.Response;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@ApiController
@RequestMapping(UrlConstraint.ProductManagement.ROOT)
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @IsAdmin
    @ValidateData
    @PostMapping(UrlConstraint.ProductManagement.CREATE)
    public Response createProduct(@Valid @RequestBody ItemDto itemDto, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        return productService.createProduct(itemDto);
    }

    @IsAdmin
    @GetMapping(UrlConstraint.CategoryManagement.GET)
    public Response getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }


    @IsAdmin
    @ValidateData
    @GetMapping(UrlConstraint.ProductManagement.GET_ALL)
    public Response getALlProducts() {
        return productService.getAllProducts();
    }

}

