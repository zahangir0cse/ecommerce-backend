package com.zahjava.ecommercebackend.model;

import com.zahjava.ecommercebackend.model.BaseModel;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class ItemRelation extends BaseModel {
    private Long itemId;
    private Long itemParentId;
}
