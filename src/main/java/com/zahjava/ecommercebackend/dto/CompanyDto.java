package com.zahjava.ecommercebackend.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class CompanyDto {
    private Long id;
    @NotBlank(message = "Name Mandatory")
    private String name;
    @NotBlank(message = "Logo Mandatory")
    private String logo;
    @NotBlank(message = "Banner Mandatory")
    private String banner;
    @NotBlank(message = "Address Mandatory")
    private String address;
    @NotBlank(message = "OwnerName Mandatory")
    private String ownerName;
    @NotBlank(message = "VatNo Mandatory")
    private String vatNo;
    @NotBlank(message = "Tin Mandatory")
    private String tin;
    @NotBlank(message = "LicenseIssuingAuth Mandatory")
    private String licenseIssuingAuth;
    @NotBlank(message = "LicenseExpirationDate Mandatory")
    private String licenseExpirationDate;
    private List<@Valid BranchDto> branchDtoList;

}
