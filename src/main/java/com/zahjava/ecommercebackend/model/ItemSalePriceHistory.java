package com.zahjava.ecommercebackend.model;

import lombok.Data;

import javax.persistence.Entity;
import java.util.Date;

@Data
@Entity
public class ItemSalePriceHistory extends BaseModel {
    private Long productId;
    private Double salePrice;
    private Date effectiveDate;
    private Date endDate;
    private Boolean isActivePrice;
    //here createdAt,createdAt manage from BaseModel
}
