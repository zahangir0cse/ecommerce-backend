package com.zahjava.ecommercebackend.aspect;

import com.zahjava.ecommercebackend.view.Response;
import com.zahjava.ecommercebackend.view.ResponseBuilder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

@Aspect
@Configuration
public class DataValidationAspect {
    private Logger logger = LoggerFactory.getLogger(DataValidationAspect.class.getName());

    @Around("@annotation(com.zahjava.ecommercebackend.annotation.ValidateData) && args(..)")
    public Response validateData(ProceedingJoinPoint joinPoint) {
        Object[] signatures = joinPoint.getArgs();
        BindingResult result = null;
        for (int i = 0; i < signatures.length; i++) {
            if (signatures[i] instanceof BindingResult) {
                result = (BindingResult) signatures[i];
                break;
            }
        }
        if (result.hasErrors()) {
            return ResponseBuilder.getFailureResponse(result, "Bean Binding error");
        }
        try {
            return (Response) joinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage());
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }
}
