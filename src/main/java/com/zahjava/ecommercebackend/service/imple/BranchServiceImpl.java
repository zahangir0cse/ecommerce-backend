package com.zahjava.ecommercebackend.service.imple;

import com.zahjava.ecommercebackend.dto.BranchDto;
import com.zahjava.ecommercebackend.dto.CompanyDto;
import com.zahjava.ecommercebackend.model.Branch;
import com.zahjava.ecommercebackend.model.Company;
import com.zahjava.ecommercebackend.repository.BranchRepository;
import com.zahjava.ecommercebackend.repository.CompanyRepository;
import com.zahjava.ecommercebackend.service.BranchService;
import com.zahjava.ecommercebackend.view.Response;
import com.zahjava.ecommercebackend.view.ResponseBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("BranchService")
public class BranchServiceImpl implements BranchService {
    private final ModelMapper modelMapper;
    private final CompanyRepository companyRepository;
    private final BranchRepository branchRepository;
    private Logger logger = LogManager.getLogger(BranchServiceImpl.class.getName());


    public BranchServiceImpl(ModelMapper modelMapper, CompanyRepository companyRepository, BranchRepository branchRepository) {
        this.modelMapper = modelMapper;
        this.companyRepository = companyRepository;
        this.branchRepository = branchRepository;
    }

    @Override
    public Response createBranch(BranchDto branchDto) {
        try {
            Branch branch = modelMapper.map(branchDto, Branch.class);

            List<Company> companies = companyRepository.findAllByIsActiveTrue();
            if (companies.isEmpty()) {
                return ResponseBuilder.getFailureResponse(HttpStatus.NO_CONTENT, "didn't find any company, first create a company");
            }
            if (branchRepository.findByNameAndIsActiveTrue(branch.getName()).isPresent()) {
                return ResponseBuilder.getFailureResponse(HttpStatus.NOT_ACCEPTABLE, "Already have one branch with this name");
            }
            Company company = companies.get(0);

            List<Branch> branchList = new ArrayList<>(company.getBranchList());
            branchList.add(branch);
            company.setBranchList(branchList);
            company = companyRepository.save(company);
            if (company != null) {
                return ResponseBuilder.getSuccessResponse(HttpStatus.CREATED, "Branch Creation Successfully", null);
            }
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @Override
    public Response updateBranch(Long branchId, BranchDto branchDto) {
        Optional<Branch> branchOptional = branchRepository.findByIdAndIsActiveTrue(branchId);
        if (!branchOptional.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, "didn't find branch");
        }
        Branch branch = branchOptional.get();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        modelMapper.map(branchDto, branch);
        branch = branchRepository.save(branch);
        if (branch != null) {
            return ResponseBuilder.getSuccessResponse(HttpStatus.OK, "Branch Updated Successfully", branch.getName());
        }

        return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

    @Override
    public Response getBranch(Long branchId) {
        Optional<Branch> optionalBranch = branchRepository.findByIdAndIsActiveTrue(branchId);
        if (!optionalBranch.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, "didn't find any branch");
        }
        try {
            BranchDto branchDto = modelMapper.map(optionalBranch.get(), BranchDto.class);
            if (branchDto != null) {
                return ResponseBuilder.getSuccessResponse(HttpStatus.OK, String.format("%s retrieved successfully", "Branch"), branchDto);
            }
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error occurred");

        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public Response getAllBranch() {

        return null;
    }
}
