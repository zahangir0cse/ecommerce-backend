package com.zahjava.ecommercebackend.model.categoryModel;

import com.zahjava.ecommercebackend.model.BaseModel;
import com.zahjava.ecommercebackend.model.Product;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Category extends BaseModel {
    private String name;
    private String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<SubCategory> subCategoryList;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> productList;
}
