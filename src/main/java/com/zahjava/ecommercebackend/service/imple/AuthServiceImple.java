package com.zahjava.ecommercebackend.service.imple;

import com.zahjava.ecommercebackend.dto.LoginDto;
import com.zahjava.ecommercebackend.dto.LoginResponseDto;
import com.zahjava.ecommercebackend.filter.JwtTokenProvider;
import com.zahjava.ecommercebackend.model.User;
import com.zahjava.ecommercebackend.repository.UserRepository;
import com.zahjava.ecommercebackend.service.AuthService;
import com.zahjava.ecommercebackend.view.Response;
import com.zahjava.ecommercebackend.view.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service("authService")
public class AuthServiceImple implements AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImple(UserRepository userRepository, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    public Response login(LoginDto loginDto, HttpServletRequest request) {
        User user = userRepository.findByUsernameAndIsActiveTrue(loginDto.getUsername());
        if (user == null) {
            return ResponseBuilder.getFailureResponse(HttpStatus.UNAUTHORIZED, "Invalid UserName or password");
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        if (authentication.isAuthenticated()) {
            LoginResponseDto loginResponseDto = new LoginResponseDto();
            loginResponseDto.setToken(jwtTokenProvider.generateToken(authentication, request));
            loginResponseDto.setUsername(user.getUsername());
            return ResponseBuilder.getSuccessResponse(HttpStatus.OK, "Logged In Success", loginResponseDto);
        }
        return ResponseBuilder.getFailureResponse(HttpStatus.BAD_REQUEST, "Invalid UserName or password");
    }
}
