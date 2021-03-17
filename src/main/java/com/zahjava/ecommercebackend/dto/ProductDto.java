package com.zahjava.ecommercebackend.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ProductDto {
    private Long id;
    @NotEmpty(message = "Product Name Mandatory")
    private String name;
    @NotEmpty(message = "Product Quantity Mandatory")
    @NotNull(message = "Quantity is Mandatory")
    @Min(1)
    private String quantity;
    @NotEmpty(message = "Product unitPriceBuy Mandatory")
    private String unitPriceBuy;
    @NotEmpty(message = "Product unitPriceSale Mandatory")
    private String unitPriceSale;
    @NotEmpty(message = "ProductRoute Mandatory")
    private String productRoute;
    private String discount;
    @NotEmpty(message = "Product Description Mandatory")
    private String description;
    private String discountNote;
    private List<DocumentDto> documentList;
}
