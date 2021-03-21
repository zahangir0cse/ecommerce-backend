package com.zahjava.ecommercebackend.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CategoryCommonDto {
    private Long id;
    @NotEmpty(message = "Name Mandatory")
    private String name;
    @NotEmpty(message = "Name Mandatory")
    private String description;
    private Long parentId;
}
