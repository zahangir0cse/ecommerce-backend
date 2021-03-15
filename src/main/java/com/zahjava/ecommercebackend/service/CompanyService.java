package com.zahjava.ecommercebackend.service;

import com.zahjava.ecommercebackend.dto.CompanyDto;
import com.zahjava.ecommercebackend.view.Response;
import org.springframework.data.domain.Pageable;

public interface CompanyService {
    Response createCompany(CompanyDto companyDto);

    Response update(Long id, CompanyDto companyDto);

    Response get(Long id);

    Response getAll(Pageable pageable, boolean isExport, String search, String status);
}
