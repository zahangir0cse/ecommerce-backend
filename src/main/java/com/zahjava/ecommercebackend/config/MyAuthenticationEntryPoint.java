package com.zahjava.ecommercebackend.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class MyAuthenticationEntryPoint /*extends BasicAuthenticationEntryPoint */implements AuthenticationEntryPoint {
    private static final Logger logger = LogManager.getLogger(MyAuthenticationEntryPoint.class.getName());

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        //response.addHeader("WWW-Authenticate", "Basic Realm=" + getRealmName());
        logger.error("Message: "+ authException.getMessage());
        response.setStatus(response.SC_UNAUTHORIZED);
        response.sendError(response.SC_UNAUTHORIZED, "Unauthorized Request");

    }

//    @Override
//    public void afterPropertiesSet() {
//        setRealmName("ECommerceProject");
//        super.afterPropertiesSet();
//    }
}
