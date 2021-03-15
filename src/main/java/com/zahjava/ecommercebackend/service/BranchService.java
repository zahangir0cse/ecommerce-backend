package com.zahjava.ecommercebackend.service;

import com.zahjava.ecommercebackend.dto.BranchDto;
import com.zahjava.ecommercebackend.view.Response;

public interface BranchService {
    Response createBranch(BranchDto branchDto);
    Response updateBranch(Long id,BranchDto branchDto);
    Response getBranch(Long branchId);
    Response getAllBranch();
}
