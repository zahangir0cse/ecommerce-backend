package com.zahjava.ecommercebackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class UserDto {
    private Long id;
    @NotEmpty(message = "First Name is Mandatory")
    private String firstName;
    @NotEmpty(message = "Last Name is Mandatory")
    private String lastname;
    @NotBlank(message = "Email field should not be empty")
    @Email(regexp = "^(.+)@(.+)$", message = "Invalid Email Pattern")
    private String email;
    @NotBlank(message = "UserName is Mandatory")
    private String username;
    @NotEmpty(message = "Phone is mandatory")
    @Pattern(regexp = "^(?:\\+?88)?01[135-9]\\d{8}$", message = "Invalid Mobile number.")
    @Size(max = 11, message = "Mobile Number Digits should be 11")
    private String mobileNo;
    @NotBlank(message = "You Have To Enter The Password")
    @Size(min = 8, max = 20, message = "Password length should be in between 8 to 20")
    @JsonProperty
    private String password;

    public String getPassword() {
        return password;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public void setPassword(String password) {
        this.password = password;
    }
}
