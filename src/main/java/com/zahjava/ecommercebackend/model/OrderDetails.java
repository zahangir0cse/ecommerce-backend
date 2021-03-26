package com.zahjava.ecommercebackend.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "product_order_details")
public class OrderDetails extends BaseModel {
    @ManyToOne
    private Item item;
    private Double quantity;
    private Double unitSalePrice;
}
