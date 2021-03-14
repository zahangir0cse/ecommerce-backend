package com.zahjava.ecommercebackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Entity
public class Branch extends BaseModel {
    private String name;
    private String address;
    private String branchHead;
    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonIgnore
    private Company company;
}
