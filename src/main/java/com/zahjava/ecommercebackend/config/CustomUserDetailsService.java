package com.zahjava.ecommercebackend.config;

import com.zahjava.ecommercebackend.filter.UserPrincipal;
import com.zahjava.ecommercebackend.model.User;
import com.zahjava.ecommercebackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =  userService.getUserByUserName(username);
        UserPrincipal userDetails = UserPrincipal.create(user);
        if (userDetails==null){
            throw new UsernameNotFoundException("UserName Not Found");
        }
        return userDetails;
    }
}
