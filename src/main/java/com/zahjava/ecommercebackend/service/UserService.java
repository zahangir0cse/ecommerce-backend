package com.zahjava.ecommercebackend.service;


import com.zahjava.ecommercebackend.dto.UserDto;
import com.zahjava.ecommercebackend.model.AuthModel.User;
import com.zahjava.ecommercebackend.view.Response;

public interface UserService {
    Response createUser(UserDto userDto);
    User getUserByUserName(String username);
    Response getAllUsers();
}
