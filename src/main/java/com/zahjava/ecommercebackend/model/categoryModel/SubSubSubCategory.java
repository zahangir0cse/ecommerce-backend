package com.zahjava.ecommercebackend.model.categoryModel;

import com.zahjava.ecommercebackend.model.BaseModel;
import com.zahjava.ecommercebackend.model.Product;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Entity
public class SubSubSubCategory extends BaseModel {
    private String name;
    private String description;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> productList;
}
