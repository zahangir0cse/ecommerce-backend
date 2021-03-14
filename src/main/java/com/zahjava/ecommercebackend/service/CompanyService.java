package com.zahjava.ecommercebackend.service;

import com.zahjava.ecommercebackend.dto.CompanyDto;
import com.zahjava.ecommercebackend.view.Response;

public interface CompanyService {
    Response createCompany(CompanyDto companyDto);
}
