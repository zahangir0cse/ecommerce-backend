package com.zahjava.ecommercebackend.service.impl;

import com.zahjava.ecommercebackend.dto.UserDto;
import com.zahjava.ecommercebackend.model.AuthModel.Role;
import com.zahjava.ecommercebackend.model.AuthModel.User;
import com.zahjava.ecommercebackend.repository.RoleRepository;
import com.zahjava.ecommercebackend.repository.UserRepository;
import com.zahjava.ecommercebackend.service.UserService;
import com.zahjava.ecommercebackend.utils.RoleConstraint;
import com.zahjava.ecommercebackend.view.Response;
import com.zahjava.ecommercebackend.view.ResponseBuilder;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service("userService")
public class UserServiceImpl implements UserService {
    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class.getName());
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    @Override
    public Response createUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        user.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setFirstName(userDto.getFirstName());
        user.setLastname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setMobileNo(userDto.getMobileNo());
        int roleCustomerCount = roleRepository.countByRoleNameAndIsActiveTrue(RoleConstraint.ROLE_USER.name());//Is this Exist?
        Role role;
        if (roleCustomerCount == 0) {
            role = new Role();
            role.setRoleName(RoleConstraint.ROLE_USER.name());//get value from enum
            role = roleRepository.save(role);
        } else {
            role = roleRepository.findByRoleNameAndIsActiveTrue(RoleConstraint.ROLE_USER.name());
        }
        user.setRoles(Collections.singletonList(role));
        user = userRepository.save(user);
        if (user != null) {
            return ResponseBuilder.getSuccessResponse(HttpStatus.CREATED, "User Created Successfully", user.getUsername());
        }
        return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

    @Override
    public User getUserByUserName(String username) {
        return userRepository.findByUsernameAndIsActiveTrue(username);
    }

    @Override
    public Response getAllUsers() {
        return null;
    }


}
