package com.zahjava.ecommercebackend.controller;

import com.zahjava.ecommercebackend.annotation.ApiController;
import com.zahjava.ecommercebackend.annotation.IsAdmin;
import com.zahjava.ecommercebackend.annotation.IsAdminOrEmployee;
import com.zahjava.ecommercebackend.annotation.ValidateData;
import com.zahjava.ecommercebackend.dto.ProductDto;
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
    public Response createProduct(@Valid @RequestBody ProductDto productDto, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        return productService.createProduct(productDto);
    }

    @IsAdmin
    @ValidateData
    @PutMapping(UrlConstraint.ProductManagement.UPDATE)
    public Response updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto productDto, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        return productService.updateProduct(id, productDto);
    }

    @IsAdmin
    @ValidateData
    @GetMapping(UrlConstraint.ProductManagement.GET)
    public Response getProduct(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        return productService.getProduct(id);
    }

    @IsAdmin
    @ValidateData
    @DeleteMapping(UrlConstraint.ProductManagement.DELETE)
    public Response deleteProduct(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        return productService.deleteProduct(id);
    }
}
