package com.zahjava.ecommercebackend.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "product_order_details")
public class OrderDetails extends BaseModel {
    @ManyToOne
    private Order order;
    private Item item;
    private Double quantity;
    private Double unitSalePrice;
}
