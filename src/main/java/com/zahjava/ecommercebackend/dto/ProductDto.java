package com.zahjava.ecommercebackend.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ProductDto {
    private Long id;
    @NotBlank(message = "Product Name Mandatory")
    private String name;
    @NotBlank(message = "Product Quantity Mandatory")
    private String quantity;
    @NotBlank(message = "Product unitPriceBuy Mandatory")
    private String unitPriceBuy;
    @NotBlank(message = "Product unitPriceSale Mandatory")
    private String unitPriceSale;
    @NotBlank(message = "ProductRoute Mandatory")
    private String productRoute;
    private String discount;
    @NotBlank(message = "Product Description Mandatory")
    private String description;
    private String discountNote;
}
