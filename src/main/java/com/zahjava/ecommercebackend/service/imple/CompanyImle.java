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
        Company company = modelMapper.map(companyDto, Company.class);
        Company haveAnyCompany = companyRepository.findByNameAndIsActiveTrue(company.getName());
        if (haveAnyCompany != null) {//have previous company with current requested name
            haveAnyCompany.setBranchList(company.getBranchList());
            haveAnyCompany = companyRepository.save(haveAnyCompany);
        }
        if (haveAnyCompany != null) {
            return ResponseBuilder.getSuccessResponse(HttpStatus.CREATED, "Company Updated Successfully", null);
        }
        /**
         * otherwise if don't have any previous company name like this requested company name
         */
        if (company.getBranchList() != null) {
            company.setBranchList(company.getBranchList());
        }
        company = companyRepository.save(company);
        if (company != null) {
            return ResponseBuilder.getSuccessResponse(HttpStatus.CREATED, "Company Creation Successfully", company.getName());
        }
        return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }
}
