package com.zahjava.ecommercebackend.dto;

import lombok.Data;

@Data
public class OrderDetailsDto extends BaseModelDto {
    private ItemDto item;
    private Double quantity;
    private Double unitSalePrice;
}
