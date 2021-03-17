package com.zahjava.ecommercebackend.dto;

import com.zahjava.ecommercebackend.model.BaseModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class BranchDto extends BaseModel {
    private Long id;
    @NotEmpty(message = "Branch Name Mandatory")
    private String name;
    @NotEmpty(message = "Address Mandatory")
    private String address;
    @NotEmpty(message = "Branch Head Mandatory")
    private String branchHeadName;
    @NotEmpty
    @Pattern(regexp = "^(?:\\+?88)?01[135-9]\\d{8}$", message = "invalid mobile number.") @Size(max = 11, message = "digits should be 11")
    private String branchMobileNo;
}
