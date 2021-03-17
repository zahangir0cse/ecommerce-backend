package com.zahjava.ecommercebackend.model.companyModel;

import com.zahjava.ecommercebackend.model.BaseModel;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Entity
public class Company extends BaseModel {
    private String name;
    private String logo;
    private String banner;
    private String address;
    private String ownerName;
    private String vatNo;
    private String tin;
    private String licenseIssuingAuth;
    private String licenseExpirationDate;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Branch> branchList;
}
