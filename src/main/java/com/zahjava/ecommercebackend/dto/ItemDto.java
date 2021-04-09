package com.zahjava.ecommercebackend.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

@Data
public class ItemDto {
    private Long id;
    @NotEmpty(message = "Name Mandatory")
    private String name;
    private Long quantity;
    private Double unitPriceBuy;
    private Double unitPriceSale;
    private String productRoute;
    private Double discount;
    private String description;
    private String discountNote;
    private List<DocumentDto> documentList;
    @Min(value = 1, message = "PurchaseQuantity should Be 1 or more")
    private Long purchaseQuantity;//from view new updated quantity
    @NotNull
    @Size(min = 2, max = 30)
    @NotEmpty(message = "PurchaseCompany Mandatory")
    private String purchaseCompany;//purchase from company
    @NotEmpty(message = "Company Address Mandatory")
    private String address;
    @DecimalMin(value = "1", message = "Purchase price shouldn't be less then 1")
    private Double purchasePrice;//form view
    @DecimalMin(value = "1", message = "SalePrice price shouldn't be less then 1")
    private Double salePrice;//form view
}
