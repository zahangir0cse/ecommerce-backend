package com.zahjava.ecommercebackend.model;

import lombok.Data;

import javax.persistence.Entity;
import java.util.Date;

@Data
@Entity
public class ItemPurchaseHistory extends BaseModel {
    private Long productId;
    private Long purchaseQuantity;//from view new updated quantity
    private Double purchasePrice;//form view
    private String purchaseCompany;//
    private String address ;
    private Date purchaseDate;
    private String purchaseBy;
}
