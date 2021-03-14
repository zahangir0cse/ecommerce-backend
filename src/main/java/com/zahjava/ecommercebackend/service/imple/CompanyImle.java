package com.zahjava.ecommercebackend.service.imple;

import com.zahjava.ecommercebackend.dto.CompanyDto;
import com.zahjava.ecommercebackend.model.Company;
import com.zahjava.ecommercebackend.repository.CompanyRepository;
import com.zahjava.ecommercebackend.service.CompanyService;
import com.zahjava.ecommercebackend.view.Response;
import com.zahjava.ecommercebackend.view.ResponseBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service("CompanyService")
public class CompanyImle implements CompanyService {
    private final ModelMapper modelMapper;
    private final CompanyRepository companyRepository;

    public CompanyImle(ModelMapper modelMapper, CompanyRepository companyRepository) {
        this.modelMapper = modelMapper;
        this.companyRepository = companyRepository;
    }

    @Override
    public Response createCompany(CompanyDto companyDto) {

        Integer alreadyExists = companyRepository.countByIsActiveTrue();
        if (alreadyExists>0){
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_ACCEPTABLE,"Already One Company Exists");
        }
        Company company = modelMapper.map(companyDto, Company.class);
        /**
         * otherwise if don't have any previous company name like this requested company name
         */
        company = companyRepository.save(company);
        if (company != null) {
            return ResponseBuilder.getSuccessResponse(HttpStatus.CREATED, "Company Creation Successfully", company.getName());
        }
        return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }
}
