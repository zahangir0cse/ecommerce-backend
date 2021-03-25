package com.zahjava.ecommercebackend.service.impl;

import com.zahjava.ecommercebackend.dto.DocumentDto;
import com.zahjava.ecommercebackend.model.Document;
import com.zahjava.ecommercebackend.repository.DocumentRepository;
import com.zahjava.ecommercebackend.service.DocumentService;
import com.zahjava.ecommercebackend.utils.DateUtils;
import com.zahjava.ecommercebackend.view.Response;
import com.zahjava.ecommercebackend.view.ResponseBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service("documentService")
public class DocumentServiceImpl implements DocumentService {
    private final ModelMapper modelMapper;
    private final DocumentRepository documentRepository;
    private final ProductRepository productRepository;
    private final String root = "Document";
    @Value("${server.file.location}")
    private String fileRoot;
    private Logger logger = LogManager.getLogger(DocumentServiceImpl.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    public DocumentServiceImpl(ModelMapper modelMapper, DocumentRepository documentRepository, ProductRepository productRepository) {
        this.modelMapper = modelMapper;
        this.documentRepository = documentRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Response create(MultipartFile[] files, String entityName, Long entityId) {
        try {
            if (files.length == 0) {
                return ResponseBuilder.getFailureResponse(HttpStatus.BAD_REQUEST, "No File/Image attached");
            }
            for (MultipartFile file : files) {
                String fileLocation = getUniqueLocation(fileRoot + "/" + DateUtils.getStringDate(new Date(), "dd_MM_yyyy") + "/" + UUID.randomUUID().toString() + "/" + file.getOriginalFilename(), file);
                if (writeFile(file, fileLocation)) {
                    Document document = new Document();
                    document.setLocation(fileLocation);
                    document.setEntityId(entityId);
                    document.setEntityName(entityName);
                    document = documentRepository.save(document);
                }
            }
            return ResponseBuilder.getSuccessResponse(HttpStatus.OK, "Document Uploaded successfully", null);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    private String getUniqueLocation(String location, MultipartFile file) {
        int countExist = documentRepository.countByLocation(location);
        if (countExist == 0) {
            return location;
        }
        return getUniqueLocation(location + "/" + DateUtils.getStringDate(new Date(), "dd_MM_yyyy") + "/" + UUID.randomUUID().toString() + "/" + file.getOriginalFilename(), file);
    }

    private Boolean writeFile(MultipartFile multipartFile, String location) {
        try {
            byte[] bytes = multipartFile.getBytes();
            File file = new File(location.substring(0, location.lastIndexOf("/")));
            if (!file.exists()) {
                file.mkdirs();
            }
            Path path = Paths.get(location);
            Files.write(path, bytes);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public Response get(Long id) {
        Optional<Document> optionalDocument = documentRepository.findByIdAndIsActiveTrue(id);

        if (!optionalDocument.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, String.format("Requested %s could not be found", root));
        }
        try {
            DocumentDto documentDto = modelMapper.map(optionalDocument.get(), DocumentDto.class);
            if (documentDto != null) {
                return ResponseBuilder.getSuccessResponse(HttpStatus.OK, String.format("%s retrieved successfully", root), documentDto);
            }
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error occurred");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public Response getAllByDomain(Long entityId, String entityName) {
        List<Document> documentList = documentRepository.findAllByEntityIdAndEntityNameAndIsActiveTrue(entityId, entityName);
        if (documentList == null || documentList.size() == 0) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, "No documents found");
        }
        return ResponseBuilder.getSuccessResponse(HttpStatus.OK, "Documents retrieved successfully", this.getResponseDtoList(documentList));
    }

    @Override
    public List<DocumentDto> getAllDtoByDomain(Long entityId, String entityName) {
        List<Document> documentList = documentRepository.findAllByEntityIdAndEntityNameAndIsActiveTrue(entityId, entityName);
        if (documentList != null) {
//        if (documentList == null || documentList.size() == 0) {
            return this.getResponseDtoList(documentList);
        }
        return new ArrayList<>();
    }

    @Override
    public Response delete(Long id) {
        Optional<Document> optionalDocument = documentRepository.findByIdAndIsActiveTrue(id);

        if (!optionalDocument.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, String.format("Requested %s could not be found", root));
        }
        try {
            Document document = optionalDocument.get();
            document.setIsActive(false);
            document = documentRepository.save(document);
            documentRepository.save(document);
            if (document != null) {
                return ResponseBuilder.getSuccessResponse(HttpStatus.OK, null, String.format("%s deleted successfully", root));
            }
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error occurred");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public Response getAll(Pageable pageable, boolean isExport, String search, String status) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Document> criteriaQuery = criteriaBuilder.createQuery(Document.class);
        Root<Document> rootEntity = criteriaQuery.from(Document.class);

        addPredicates(criteriaBuilder, criteriaQuery, rootEntity, search);

        TypedQuery<Document> typedQuery = entityManager.createQuery(criteriaQuery);
        return getAllResponse(criteriaQuery, typedQuery, pageable, isExport);
    }

    private Response getAllResponse(CriteriaQuery<Document> criteriaQuery, TypedQuery<Document> typedQuery, Pageable pageable, boolean isExport) {
        int totalRows = this.getTotalRows(criteriaQuery);
        if (!isExport) {
            typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            typedQuery.setMaxResults(pageable.getPageSize());
        }
        long rows = typedQuery.getResultList().size();
        Page<Document> documents = new PageImpl<>(typedQuery.getResultList(), pageable, rows);

        if (!documents.hasContent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, String.format("%s not found", root));
        }
        List<DocumentDto> responseDtos = this.getResponseDtoList(documents);
        return ResponseBuilder.getSuccessResponse(HttpStatus.OK, String.format("%s list", root), responseDtos, totalRows);
    }

    private List<DocumentDto> getResponseDtoList(Page<Document> documents) {
        List<DocumentDto> responseDtos = new ArrayList<>();
        documents.forEach(document -> {
            DocumentDto dto = modelMapper.map(document, DocumentDto.class);
            responseDtos.add(dto);
        });
        return responseDtos;
    }

    private List<DocumentDto> getResponseDtoList(List<Document> documents) {
        List<DocumentDto> responseDtos = new ArrayList<>();
        documents.forEach(document -> {
            DocumentDto dto = modelMapper.map(document, DocumentDto.class);
            responseDtos.add(dto);
        });
        return responseDtos;
    }

    private int getTotalRows(CriteriaQuery<Document> criteriaQuery) {
        TypedQuery<Document> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList().size();
    }

    private void addPredicates(CriteriaBuilder criteriaBuilder, CriteriaQuery<Document> criteriaQuery, Root<Document> rootEntity, String search) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.isTrue(rootEntity.<Boolean>get("isActive")));

        if (search != null && search.trim().length() > 0) {
            Predicate pLike = criteriaBuilder.or(
                    criteriaBuilder.like(rootEntity.<String>get("entityName"), "%" + search + "%"));

//            Predicate pEqual = criteriaBuilder.equal(rootEntity.<Company>get("course").get("id"), search);

            predicates.add(pLike);
        }

        if (predicates.isEmpty()) {
            criteriaQuery.select(rootEntity);
        } else {
            criteriaQuery.select(rootEntity).where(predicates.toArray(new Predicate[predicates.size()]));
        }
    }
}
