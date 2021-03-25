package com.zahjava.ecommercebackend.dto;

import lombok.Data;

@Data
public class OrderDetailsDto extends BaseModelDto {
    private OrderDto orderDto;
    private ItemDto itemDto;
    private Double quantity;
    private Double unitSalePrice;
}
