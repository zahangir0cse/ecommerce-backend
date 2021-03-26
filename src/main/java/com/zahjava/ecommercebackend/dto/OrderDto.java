package com.zahjava.ecommercebackend.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDto extends BaseModelDto {
    private Double totalPrice;
    private Integer orderType;
    private List<OrderDetailsDto> orderDetailsList;
}
