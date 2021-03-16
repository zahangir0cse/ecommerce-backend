package com.zahjava.ecommercebackend.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DocumentDto {
    private Long id;
    private String location;
    private String entityName;
    private Long entityId;
}
