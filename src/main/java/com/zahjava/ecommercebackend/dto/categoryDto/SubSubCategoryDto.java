package com.zahjava.ecommercebackend.dto.categoryDto;

import com.zahjava.ecommercebackend.dto.ProductDto;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SubSubCategoryDto {
    private Long id;
    @NotNull
    @NotEmpty(message = "SubSubSubCategory Mandatory")
    private String name;
    @NotEmpty(message = "Description Mandatory")
    private String description;
    private List<SubSubSubCategoryDto> subSubSubCategoryList;
    private List<ProductDto> productList;
}
