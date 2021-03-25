package com.zahjava.ecommercebackend.service;

import com.zahjava.ecommercebackend.dto.ItemDto;
import com.zahjava.ecommercebackend.view.Response;

public interface ProductService {
    Response createProduct(ItemDto itemDto);

    Response getAllProducts();

    Response getProductById(Long id);
}
