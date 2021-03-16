package com.zahjava.ecommercebackend.service;

import com.zahjava.ecommercebackend.view.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {

    Response create(MultipartFile[] files, String entityName, Long entityId);

//    Response update(Long id, MultipartFile[] files, String entityName, Long entityId);

    Response get(Long id);

    Response getAllByDomain(Long entityId, String entityName);

    Response delete(Long id);

    Response getAll(Pageable pageable, boolean isExport, String search, String status);
}
