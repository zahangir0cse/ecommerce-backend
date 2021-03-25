package com.zahjava.ecommercebackend.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BaseModelDto {
    private Long id;
    private String createdBy;
    private Date createdAt;
    private String updatedBy;
    private Date updatedAt;
    private Integer activeStatus;
    private Boolean isActive;
}
