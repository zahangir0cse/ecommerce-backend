package com.zahjava.ecommercebackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Role extends BaseModel {
    private String roleName;
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<User> users = new ArrayList<>();
}
