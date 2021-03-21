package com.zahjava.ecommercebackend.model;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class Item extends BaseModel {
    private String name;
    private Long quantity;
    private Double unitPriceBuy;
    private Double unitPriceSale;
    private String productRoute;
    private Double discount;
    private String description;
    private String discountNote;
    private Boolean isProduct;
}
