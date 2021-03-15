package com.zahjava.ecommercebackend.controller;

import com.zahjava.ecommercebackend.annotation.ApiController;
import com.zahjava.ecommercebackend.annotation.IsAdmin;
import com.zahjava.ecommercebackend.annotation.ValidateData;
import com.zahjava.ecommercebackend.dto.BranchDto;
import com.zahjava.ecommercebackend.service.BranchService;
import com.zahjava.ecommercebackend.utils.UrlConstraint;
import com.zahjava.ecommercebackend.view.Response;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@ApiController
@RequestMapping(UrlConstraint.BranchManagement.ROOT)
public class BranchController {
    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @IsAdmin
    @ValidateData
    @PostMapping(UrlConstraint.BranchManagement.CREATE)
    public Response createBranch(@Valid @RequestBody BranchDto branchDto, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {
        return branchService.createBranch(branchDto);
    }

    @IsAdmin
    @ValidateData
    @PutMapping(UrlConstraint.BranchManagement.UPDATE)
    public Response updateBranch(@PathVariable Long id, @Valid @RequestBody BranchDto branchDto, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {
        return branchService.updateBranch(id, branchDto);
    }

    @IsAdmin
    @GetMapping(UrlConstraint.BranchManagement.GET)
    public Response getBranchById(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        return branchService.getBranch(id);
    }
}
