package com.zahjava.ecommercebackend.repository;

import com.zahjava.ecommercebackend.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findByNameAndIsActiveTrue(String companyName);

    Integer countByIsActiveTrue();
}
