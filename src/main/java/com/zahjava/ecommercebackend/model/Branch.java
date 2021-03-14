package com.zahjava.ecommercebackend.model;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class Branch extends BaseModel {
    private String name;
    private String address;
    private String branchHeadName;
    private String branchMobileNo;
}
