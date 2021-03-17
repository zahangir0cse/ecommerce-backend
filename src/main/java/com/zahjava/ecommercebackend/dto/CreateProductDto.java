package com.zahjava.ecommercebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class CreateProductDto {
    private Long productId;
    @NotEmpty(message = "EntityName Mandatory")
    private String entityName;
}
