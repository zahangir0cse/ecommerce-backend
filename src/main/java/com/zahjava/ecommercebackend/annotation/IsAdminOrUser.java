package com.zahjava.ecommercebackend.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
//@RolesAllowed({"ROLE_ADMIN", "ROLE_CUSTOMER"})
@PreAuthorize("hasRole('ROLE_ADMIN' or hasRole('ROLE_USER'))")
public @interface IsAdminOrUser {
}
