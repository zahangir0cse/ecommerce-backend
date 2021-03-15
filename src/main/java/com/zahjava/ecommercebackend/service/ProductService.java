package com.zahjava.ecommercebackend.service;

import com.zahjava.ecommercebackend.dto.ProductDto;
import com.zahjava.ecommercebackend.view.Response;

public interface ProductService {
    Response createProduct(ProductDto productDto);

    Response updateProduct(Long productId, ProductDto productDto);

    Response getProduct(Long productId);

    Response deleteProduct(Long id);
}
