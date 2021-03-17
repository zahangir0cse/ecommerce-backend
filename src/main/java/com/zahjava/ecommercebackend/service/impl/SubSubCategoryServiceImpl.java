package com.zahjava.ecommercebackend.service.impl;

import com.zahjava.ecommercebackend.dto.CreateProductDto;
import com.zahjava.ecommercebackend.dto.ProductDto;
import com.zahjava.ecommercebackend.dto.categoryDto.SubSubCategoryDto;
import com.zahjava.ecommercebackend.model.Product;
import com.zahjava.ecommercebackend.model.categoryModel.SubSubCategory;
import com.zahjava.ecommercebackend.repository.ProductRepository;
import com.zahjava.ecommercebackend.repository.SubSubCategoryRepository;
import com.zahjava.ecommercebackend.service.SubSubCategoryService;
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

@Service("SubSubCategoryService")
public class SubSubCategoryServiceImpl implements SubSubCategoryService {
    private final Logger logger = LoggerFactory.getLogger(SubSubCategoryServiceImpl.class);
    private final SubSubCategoryRepository subSubCategoryRepository;
    private final ModelMapper modelMapper;
    private final SubSubSubCategoryImple subSubSubCategoryImple;
    private final ProductRepository productRepository;

    public SubSubCategoryServiceImpl(SubSubCategoryRepository subSubCategoryRepository, ModelMapper modelMapper, SubSubSubCategoryImple subSubSubCategoryImple, ProductRepository productRepository) {
        this.subSubCategoryRepository = subSubCategoryRepository;
        this.modelMapper = modelMapper;
        this.subSubSubCategoryImple = subSubSubCategoryImple;
        this.productRepository = productRepository;
    }

    @Override
    public Response createSubSubCategory(SubSubCategoryDto subSubCategoryDto) {
        Optional<SubSubCategory> subSubCategoryOptional = subSubCategoryRepository.findByNameAndIsActiveTrue(subSubCategoryDto.getName());
        if (subSubCategoryOptional.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.FOUND, "Already Have A subSubCategory With This Name");
        }
        try {
            SubSubCategory subSubCategory = modelMapper.map(subSubCategoryDto, SubSubCategory.class);
            subSubCategory.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            subSubCategory = subSubCategoryRepository.save(subSubCategory);
            if (subSubCategory != null) {
                if (subSubCategory.getProductList().isEmpty()) {
                    return ResponseBuilder.getSuccessResponse(HttpStatus.CREATED, "SubSubCategory Creation Successfully", subSubCategory.getName());
                }
                List<CreateProductDto> createProductDtoList = new ArrayList<>();
                subSubCategory.getProductList().forEach(product -> {
                    CreateProductDto createProductDto = new CreateProductDto(product.getId(), Product.class.getSimpleName());
                    createProductDtoList.add(createProductDto);
                });
                return ResponseBuilder.getSuccessResponse(HttpStatus.CREATED, "SubSubCategory Creation Successfully", createProductDtoList);
            }
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error occurred");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public Response updateSubSubCategory(Long id, SubSubCategoryDto subSubCategoryDto) {
        Optional<SubSubCategory> subSubCategoryOptional = subSubCategoryRepository.findByIdAndIsActiveTrue(id);
        if (subSubCategoryOptional.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, "didn't find any subSubCategory");
        }
        try {
            SubSubCategory subSubCategory = subSubCategoryOptional.get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(subSubCategoryDto, subSubCategory);
            subSubCategory.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            subSubCategory = subSubCategoryRepository.save(subSubCategory);
            if (subSubCategory != null) {
                return ResponseBuilder.getSuccessResponse(HttpStatus.OK, "SubSubCategory Successfully Updated", subSubCategory.getName());
            }
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error occurred");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public Response getProductsBySubSubCategoryId(Long subSubCategoryId) {
        Optional<SubSubCategory> subSubCategoryOptional = subSubCategoryRepository.findByIdAndIsActiveTrue(subSubCategoryId);
        if (!subSubCategoryOptional.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, "didn't find any subSubCategory");
        }
        try {
            SubSubCategory subSubCategory = subSubCategoryOptional.get();
            List<Product> productList = subSubCategory.getProductList();
            List<ProductDto> productDtoList = subSubSubCategoryImple.getProductList(productList);
            if (productDtoList.isEmpty()) {
                return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, "didn't get any product with this subSubCategory");
            }
            int numberOfRow = productRepository.countAllByIsActiveTrue();
            return ResponseBuilder.getSuccessResponse(HttpStatus.OK, "Product Retrieved Successfully", productDtoList, numberOfRow);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public Response getAllSubSubCategories() {
        Optional<List<SubSubCategory>> subSubCategoryList = subSubCategoryRepository.findAllByIsActiveTrue();
        if (subSubCategoryList.isPresent()) {
            List<SubSubCategoryDto> subSubCategoryDtos = this.getAllSubSubCategoriesDto(subSubCategoryList.get());
            int numberOfRow = subSubCategoryRepository.countAllByIsActiveTrue();
            if (!subSubCategoryDtos.isEmpty()) {
                return ResponseBuilder.getSuccessResponse(HttpStatus.OK, "SubSubCategories Retrieved Successfully", subSubCategoryDtos, numberOfRow);
            }
        }
        return ResponseBuilder.getSuccessResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", null);
    }


    private List<SubSubCategoryDto> getAllSubSubCategoriesDto(List<SubSubCategory> subSubCategories) {
        List<SubSubCategoryDto> subSubCategoryDtos = new ArrayList<>();
        subSubCategories.forEach(subSubCategory -> {
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            SubSubCategoryDto subSubCategoryDto = modelMapper.map(subSubCategory, SubSubCategoryDto.class);
            if (!subSubCategoryDto.getProductList().isEmpty()) {
                /**
                 * For set each product document list if have any product with this subSubCategory
                 */
                subSubCategoryDto.setProductList(subSubSubCategoryImple.getProductList(subSubCategory.getProductList()));
            }
            subSubCategoryDtos.add(subSubCategoryDto);
        });
        return subSubCategoryDtos;
    }
}
