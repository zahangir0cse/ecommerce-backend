package com.zahjava.ecommercebackend.service;



import com.zahjava.ecommercebackend.dto.LoginDto;
import com.zahjava.ecommercebackend.view.Response;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    Response login(LoginDto loginDto, HttpServletRequest request);
}
