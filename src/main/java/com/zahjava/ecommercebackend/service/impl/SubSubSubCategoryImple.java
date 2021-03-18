package com.zahjava.ecommercebackend.service.impl;

import com.zahjava.ecommercebackend.dto.CreateProductDto;
import com.zahjava.ecommercebackend.dto.DocumentDto;
import com.zahjava.ecommercebackend.dto.ProductDto;
import com.zahjava.ecommercebackend.dto.categoryDto.SubSubSubCategoryDto;
import com.zahjava.ecommercebackend.model.Product;
import com.zahjava.ecommercebackend.model.categoryModel.SubSubSubCategory;
import com.zahjava.ecommercebackend.repository.ProductRepository;
import com.zahjava.ecommercebackend.repository.SubSubSubCategoryRepository;
import com.zahjava.ecommercebackend.service.DocumentService;
import com.zahjava.ecommercebackend.service.SubSubSubCategoryService;
import com.zahjava.ecommercebackend.view.Response;
import com.zahjava.ecommercebackend.view.ResponseBuilder;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("subSubSubCategoryService")
public class SubSubSubCategoryImple implements SubSubSubCategoryService {
    private final Logger logger = LoggerFactory.getLogger(SubSubSubCategoryImple.class);
    private final ModelMapper modelMapper;
    private final SubSubSubCategoryRepository subSubSubCategoryRepository;
    private final ProductRepository productRepository;
    private final DocumentService documentService;

    public SubSubSubCategoryImple(ModelMapper modelMapper, SubSubSubCategoryRepository subSubSubCategoryRepository, ProductRepository productRepository, DocumentService documentService) {
        this.modelMapper = modelMapper;
        this.subSubSubCategoryRepository = subSubSubCategoryRepository;
        this.productRepository = productRepository;
        this.documentService = documentService;
    }

    @Override
    public Response createSubSubSubCategory(SubSubSubCategoryDto subSubSubCategoryDto) {
        Optional<SubSubSubCategory> subSubSubCategoryOptional = subSubSubCategoryRepository.findByNameAndIsActiveTrue(subSubSubCategoryDto.getName());
        if (subSubSubCategoryOptional.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.FOUND, "Already Have A subSubSubCategory With This Name");
        }
        try {
            SubSubSubCategory subSubSubCategory = modelMapper.map(subSubSubCategoryDto, SubSubSubCategory.class);
            subSubSubCategory.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            subSubSubCategory.setCreatedAt(new Date());
            subSubSubCategory = subSubSubCategoryRepository.save(subSubSubCategory);
            if (subSubSubCategory != null) {
                if (subSubSubCategory.getProductList().isEmpty()) {
                    return ResponseBuilder.getSuccessResponse(HttpStatus.CREATED, "SubSubSubCategory Creation Successfully", subSubSubCategory.getName());
                }
                List<CreateProductDto> createProductDtoList = new ArrayList<>();
                subSubSubCategory.getProductList().forEach(product -> {
                    CreateProductDto createProductDto = new CreateProductDto(product.getId(), Product.class.getSimpleName());
                    createProductDtoList.add(createProductDto);
                });
                return ResponseBuilder.getSuccessResponse(HttpStatus.CREATED, "SubSubSubCategory Creation Successfully", createProductDtoList);
            }
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error occurred");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public Response updateSubSubSubCategory(Long id, SubSubSubCategoryDto subSubSubCategoryDto) {
        Optional<SubSubSubCategory> subSubSubCategoryOptional = subSubSubCategoryRepository.findByIdAndIsActiveTrue(id);
        if (!subSubSubCategoryOptional.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, "didn't find any subSubSubCategory");
        }
        try {
            SubSubSubCategory subSubSubCategory = subSubSubCategoryOptional.get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(subSubSubCategoryDto, subSubSubCategory);
            subSubSubCategory.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            subSubSubCategory = subSubSubCategoryRepository.save(subSubSubCategory);
            if (subSubSubCategory != null) {
                return ResponseBuilder.getSuccessResponse(HttpStatus.OK, "SubSubSubCategory Successfully Updated", subSubSubCategory.getName());
            }
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error occurred");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public Response getProductsBySubSubSubCategoryId(Long id) {
        Optional<SubSubSubCategory> subSubSubCategoryOptional = subSubSubCategoryRepository.findByIdAndIsActiveTrue(id);
        if (!subSubSubCategoryOptional.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, "didn't find any subSubSubCategory");
        }
        try {
            SubSubSubCategory subSubSubCategory = subSubSubCategoryOptional.get();
            List<Product> productList = subSubSubCategory.getProductList();
            List<ProductDto> productDtoList = this.getProductList(productList);
            if (productDtoList.isEmpty()) {
                return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, "didn't get any product with this subSubSubCategory");
            }
            int numberOfRow = productRepository.countAllByIsActiveTrue();
            return ResponseBuilder.getSuccessResponse(HttpStatus.OK, "Product Retrieved Successfully", productDtoList, numberOfRow);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @Override
    public Response getAllCategories() {
        Optional<List<SubSubSubCategory>> optionalSubSubSubCategoryList = subSubSubCategoryRepository.findAllByIsActiveTrue();
        List<SubSubSubCategoryDto> subSubSubCategoryDtos = this.getAllSubSubSubCategories(optionalSubSubSubCategoryList.get());
        int numberOfRow = subSubSubCategoryRepository.countAllByIsActiveTrue();
        if (!subSubSubCategoryDtos.isEmpty()) {
            return ResponseBuilder.getSuccessResponse(HttpStatus.OK, "SubSubSubCategory Retrieved Successfully", subSubSubCategoryDtos, numberOfRow);
        }
        return ResponseBuilder.getSuccessResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", null);
    }

    private List<SubSubSubCategoryDto> getAllSubSubSubCategories(List<SubSubSubCategory> subSubSubCategories) {
        List<SubSubSubCategoryDto> subSubSubCategoryDtos = new ArrayList<>();
        subSubSubCategories.forEach(subSubSubCategory -> {
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            SubSubSubCategoryDto subSubSubCategoryDto = modelMapper.map(subSubSubCategory, SubSubSubCategoryDto.class);
            if (!subSubSubCategoryDto.getProductList().isEmpty()) {
                /**
                 * For set each product document list if have any product with this subSUbSubCategory
                 */
                subSubSubCategoryDto.setProductList(getProductList(subSubSubCategory.getProductList()));
            }
            subSubSubCategoryDtos.add(subSubSubCategoryDto);
        });
        return subSubSubCategoryDtos;
    }

    @Override
    public List<ProductDto> getProductList(List<Product> productList) {
        List<ProductDto> productDtoList = new ArrayList<>();
        productList.forEach(product -> {
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            ProductDto productDto = modelMapper.map(product, ProductDto.class);
            List<DocumentDto> documentList = documentService.getAllDtoByDomain(productDto.getId(), Product.class.getSimpleName());//for get image for a product by needed param
            productDto.setDocumentList(documentList);
            productDtoList.add(productDto);
        });
        return productDtoList;
    }

}
