package com.zahjava.ecommercebackend.repository;


import com.zahjava.ecommercebackend.model.AuthModel.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleNameAndIsActiveTrue(String roleName);
    int countByRoleNameAndIsActiveTrue(String roleName);
}
