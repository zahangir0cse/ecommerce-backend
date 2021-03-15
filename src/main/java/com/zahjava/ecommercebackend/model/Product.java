package com.zahjava.ecommercebackend.model;

import lombok.Data;

import javax.persistence.Entity;
@Data
@Entity
public class Product extends BaseModel {
    private String name;
    private String quantity;
    private String unitPriceBuy;
    private String unitPriceSale;
    private String productRoute;
    private String discount;
    private String description;
    private String discountNote;
}
