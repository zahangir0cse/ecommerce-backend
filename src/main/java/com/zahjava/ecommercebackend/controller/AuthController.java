package com.zahjava.ecommercebackend.controller;

import com.zahjava.ecommercebackend.annotation.ApiController;
import com.zahjava.ecommercebackend.annotation.ValidateData;
import com.zahjava.ecommercebackend.dto.LoginDto;
import com.zahjava.ecommercebackend.service.AuthService;
import com.zahjava.ecommercebackend.utils.UrlConstraint;
import com.zahjava.ecommercebackend.view.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@ApiController
@RequestMapping(UrlConstraint.AuthManagement.ROOT)
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @ValidateData
    @PostMapping(UrlConstraint.AuthManagement.LOGIN)
    public Response login(@RequestBody @Valid LoginDto loginDto, BindingResult result, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return authService.login(loginDto, httpServletRequest);
    }
}
