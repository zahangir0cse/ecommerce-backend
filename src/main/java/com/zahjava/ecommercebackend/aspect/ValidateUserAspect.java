package com.zahjava.ecommercebackend.aspect;

import com.zahjava.ecommercebackend.model.AuthModel.User;
import com.zahjava.ecommercebackend.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Configuration
public class ValidateUserAspect {
    @Value("${server.servlet.context-path}")
    private String URL_CONTEXT;
    private Logger logger = LogManager.getLogger(ValidateUserAspect.class.getName());
    private final UserRepository userRepository;

    public ValidateUserAspect(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @After(value = "execution(public * com.zahjava.ecommercebackend.controller.AuthController.*(..))")
    public void getMethodData(JoinPoint joinPoint) {

        Object[] signatures = joinPoint.getArgs();
        HttpServletRequest incomingRequest = null;
        for (int i = 0; i < signatures.length; i++) {
            if (signatures[i] instanceof HttpServletRequest) {
                incomingRequest = (HttpServletRequest) signatures[i];
                break;
            }
        }
        String requestedUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameAndIsActiveTrue(requestedUserName);
        if (user != null) {
            if (incomingRequest != null ) {
                logger.info("\n CRUD By  = " + requestedUserName +
                        "\nMethod is = " + joinPoint.getSignature().getName() +
                        "\nRequested url is = " + incomingRequest.getRequestURI() +
                        "\n" + "IP Address = " + incomingRequest.getRemoteAddr());
            }
        }
    }
}
