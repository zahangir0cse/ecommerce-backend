package com.zahjava.ecommercebackend.repository;

import com.zahjava.ecommercebackend.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    Optional<Branch> findByNameAndIsActiveTrue(String name);
    Optional<Branch> findByIdAndIsActiveTrue(Long id);
}
