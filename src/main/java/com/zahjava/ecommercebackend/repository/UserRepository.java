package com.zahjava.ecommercebackend.repository;


import com.zahjava.ecommercebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameAndIsActiveTrue(String username);

    User findByIdAndIsActiveTrue(Long id);
}
