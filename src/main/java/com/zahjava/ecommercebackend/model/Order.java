package com.zahjava.ecommercebackend.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "product_order")
public class Order extends BaseModel {
    private Double totalPrice;
    private Integer orderType;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderDetails> orderDetailsList;
}
