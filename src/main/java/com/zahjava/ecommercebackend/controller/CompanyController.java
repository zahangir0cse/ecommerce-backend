package com.zahjava.ecommercebackend.controller;

import com.zahjava.ecommercebackend.annotation.ApiController;
import com.zahjava.ecommercebackend.annotation.IsAdmin;
import com.zahjava.ecommercebackend.annotation.ValidateData;
import com.zahjava.ecommercebackend.dto.CompanyDto;
import com.zahjava.ecommercebackend.service.CompanyService;
import com.zahjava.ecommercebackend.utils.UrlConstraint;
import com.zahjava.ecommercebackend.view.Response;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@ApiController
@RequestMapping(UrlConstraint.CompanyManagement.ROOT)
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @IsAdmin
    @ValidateData
    @PostMapping(UrlConstraint.CompanyManagement.CREATE)
    public Response createCompany(@Valid @RequestBody CompanyDto companyDto, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        return companyService.createCompany(companyDto);
    }

}
