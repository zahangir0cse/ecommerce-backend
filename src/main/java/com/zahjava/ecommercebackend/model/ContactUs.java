package com.zahjava.ecommercebackend.model;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class ContactUs extends BaseModel{
    private String name;
    private String email;
    private String message;
    private String subject;
    private String status;
}
