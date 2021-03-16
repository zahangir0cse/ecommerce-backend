package com.zahjava.ecommercebackend.model;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class Document extends BaseModel{
    private String location;
    private String entityName;
    private Long entityId;
}
