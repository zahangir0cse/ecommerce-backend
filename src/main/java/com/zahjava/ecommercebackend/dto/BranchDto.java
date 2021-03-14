package com.zahjava.ecommercebackend.dto;

import com.zahjava.ecommercebackend.model.BaseModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BranchDto extends BaseModel {
    @NotBlank(message = "Branch Name Mandatory")
    private String name;
    @NotBlank(message = "Address Mandatory")
    private String address;
    @NotBlank(message = "Branch Head Mandatory")
    private String branchHead;
}
