package com.zahjava.ecommercebackend.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class User extends BaseModel {
    private String title;
    private String firstName;
    private String lastname;
    private String email;
    private String username;
    private String mobileNo;
    private String password;
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @ToString.Exclude
    private List<Role> roles;
}
