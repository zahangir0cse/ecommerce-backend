package com.zahjava.ecommercebackend.repository;

import com.zahjava.ecommercebackend.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findAllByEntityIdAndEntityNameAndIsActiveTrue(Long entityId, String entityName);
    int countByLocation(String location);
    Optional<Document> findByIdAndIsActiveTrue(Long id);
}
