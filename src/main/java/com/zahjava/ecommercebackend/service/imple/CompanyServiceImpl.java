package com.zahjava.ecommercebackend.service.imple;

import com.zahjava.ecommercebackend.dto.CompanyDto;
import com.zahjava.ecommercebackend.model.Company;
import com.zahjava.ecommercebackend.repository.CompanyRepository;
import com.zahjava.ecommercebackend.service.CompanyService;
import com.zahjava.ecommercebackend.view.Response;
import com.zahjava.ecommercebackend.view.ResponseBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("CompanyService")
public class CompanyServiceImpl implements CompanyService {
    private final ModelMapper modelMapper;
    private final CompanyRepository companyRepository;
    private final String root = "Company";
    private Logger logger = LogManager.getLogger(CompanyServiceImpl.class.getName());
    @PersistenceContext
    private EntityManager entityManager;

    public CompanyServiceImpl(ModelMapper modelMapper, CompanyRepository companyRepository) {
        this.modelMapper = modelMapper;
        this.companyRepository = companyRepository;
    }

    @Override
    public Response createCompany(CompanyDto companyDto) {
        Integer alreadyExists = companyRepository.countByIsActiveTrue();
        if (alreadyExists > 0) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_ACCEPTABLE, "Already One Company Exists");
        }
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        Company company = modelMapper.map(companyDto, Company.class);
        /**
         * otherwise if don't have any previous company name like this requested company name
         */
        company = companyRepository.save(company);
        if (company != null) {
            return ResponseBuilder.getSuccessResponse(HttpStatus.CREATED, "Company Creation Successfully", company.getName());
        }
        return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

    @Override
    public Response update(Long id, CompanyDto companyDto) {
        Optional<Company> companyOptional = companyRepository.findByIdAndIsActiveTrue(id);
        if (!companyOptional.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, String.format("Requested %s could not be found", root));
        }

        try {
            Company company = companyOptional.get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(companyDto, company);
            company = companyRepository.save(company);
            if (company != null) {
                //Log Writing Code will goes here
                return ResponseBuilder.getSuccessResponse(HttpStatus.OK, null, String.format("%s updated successfully", root));
            }
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error occurred");

        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public Response get(Long id) {
        Optional<Company> optionalCompany = companyRepository.findByIdAndIsActiveTrue(id);

        if (!optionalCompany.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, String.format("Requested %s could not be found", root));
        }
        try {
            CompanyDto companyDto = modelMapper.map(optionalCompany.get(), CompanyDto.class);
            if (companyDto != null) {
                return ResponseBuilder.getSuccessResponse(HttpStatus.OK, String.format("%s retrieved successfully", root), companyDto);
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
        CriteriaQuery<Company> criteriaQuery = criteriaBuilder.createQuery(Company.class);
        Root<Company> rootEntity = criteriaQuery.from(Company.class);

        addPredicates(criteriaBuilder, criteriaQuery, rootEntity, search);

        TypedQuery<Company> typedQuery = entityManager.createQuery(criteriaQuery);
        return getAllResponse(criteriaQuery, typedQuery, pageable, isExport);
    }

    private Response getAllResponse(CriteriaQuery<Company> criteriaQuery, TypedQuery<Company> typedQuery, Pageable pageable, boolean isExport) {
        int totalRows = this.getTotalRows(criteriaQuery);
        if (!isExport) {
            typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            typedQuery.setMaxResults(pageable.getPageSize());
        }
        long rows = typedQuery.getResultList().size();
        Page<Company> students = new PageImpl<>(typedQuery.getResultList(), pageable, rows);

        if (!students.hasContent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, String.format("%s not found", root));
        }
        List<CompanyDto> responseDtos = this.getResponseDtoList(students);
        return ResponseBuilder.getSuccessResponse(HttpStatus.OK, String.format("%s list", root), responseDtos, totalRows);
    }

    private List<CompanyDto> getResponseDtoList(Page<Company> companies) {
        List<CompanyDto> responseDtos = new ArrayList<>();
        companies.forEach(company -> {
            CompanyDto dto = modelMapper.map(company, CompanyDto.class);
            responseDtos.add(dto);
        });
        return responseDtos;
    }

    private int getTotalRows(CriteriaQuery<Company> criteriaQuery) {
        TypedQuery<Company> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList().size();
    }

    private void addPredicates(CriteriaBuilder criteriaBuilder, CriteriaQuery<Company> criteriaQuery, Root<Company> rootEntity, String search) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.isTrue(rootEntity.<Boolean>get("active")));

        if (search != null && search.trim().length() > 0) {
            Predicate pLike = criteriaBuilder.or(
                    criteriaBuilder.like(rootEntity.<String>get("name"), "%" + search + "%"),
                    criteriaBuilder.like(rootEntity.<String>get("ownerName"), "%" + search + "%"));

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
