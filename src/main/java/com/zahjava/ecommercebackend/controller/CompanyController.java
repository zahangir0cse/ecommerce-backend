package com.zahjava.ecommercebackend.controller;

import com.zahjava.ecommercebackend.annotation.ApiController;
import com.zahjava.ecommercebackend.annotation.IsAdmin;
import com.zahjava.ecommercebackend.annotation.ValidateData;
import com.zahjava.ecommercebackend.dto.CompanyDto;
import com.zahjava.ecommercebackend.service.CompanyService;
import com.zahjava.ecommercebackend.utils.UrlConstraint;
import com.zahjava.ecommercebackend.view.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @IsAdmin
    @ValidateData
    @PostMapping(UrlConstraint.CompanyManagement.UPDATE)
    public Response update(@PathVariable Long id, @Valid @RequestBody CompanyDto companyDto, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        return companyService.update(id, companyDto);
    }

    @GetMapping(UrlConstraint.CompanyManagement.GET)
    public Response get(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        return companyService.get(id);
    }

    @GetMapping(UrlConstraint.CompanyManagement.GET_ALL)
    public Response getAll(HttpServletResponse httpServletResponse, Pageable pageable,
                           @RequestParam(value = "export", defaultValue = "false") boolean isExport,
                           @RequestParam(value = "search", defaultValue = "") String search,
                           @RequestParam(value = "status", defaultValue = "") String status ) {

        Response response = companyService.getAll(pageable, isExport, search, status);
        httpServletResponse.setStatus(response.getStatusCode());
        return response;
    }

}
