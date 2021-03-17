package com.zahjava.ecommercebackend.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class DocumentDto {
    private Long id;
    @NotEmpty(message = "Location Mandatory")
    private String location;
    @NotEmpty(message = "Entity Name Mandatory")
    private String entityName;
    private Long entityId;
}
