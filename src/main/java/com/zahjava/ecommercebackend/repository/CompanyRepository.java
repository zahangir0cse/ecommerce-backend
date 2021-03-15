package com.zahjava.ecommercebackend.repository;

import com.zahjava.ecommercebackend.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findByNameAndIsActiveTrue(String companyName);

    Integer countByIsActiveTrue();

    Optional<Company> findByIdAndIsActiveTrue(Long id);

    List<Company> findAllByIsActiveTrue();
}
