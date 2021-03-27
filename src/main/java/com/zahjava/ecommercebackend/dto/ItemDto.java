package com.zahjava.ecommercebackend.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
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
//    private List<ItemDto> itemDtoList;
}
