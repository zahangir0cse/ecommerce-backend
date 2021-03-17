package com.zahjava.ecommercebackend.service.impl;

import com.zahjava.ecommercebackend.dto.CreateProductDto;
import com.zahjava.ecommercebackend.dto.ProductDto;
import com.zahjava.ecommercebackend.dto.categoryDto.SubCategoryDto;
import com.zahjava.ecommercebackend.model.Product;
import com.zahjava.ecommercebackend.model.categoryModel.SubCategory;
import com.zahjava.ecommercebackend.repository.ProductRepository;
import com.zahjava.ecommercebackend.repository.SubCategoryRepository;
import com.zahjava.ecommercebackend.service.SubCategoryService;
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

@Service("SubCategoryService")
public class SubCategoryServiceImpl implements SubCategoryService {
    private final Logger logger = LoggerFactory.getLogger(SubCategoryServiceImpl.class);
    private final SubCategoryRepository subCategoryRepository;
    private final ModelMapper modelMapper;
    private final SubSubSubCategoryImple subSubSubCategoryImple;
    private final ProductRepository productRepository;

    public SubCategoryServiceImpl(SubCategoryRepository subCategoryRepository, ModelMapper modelMapper, SubSubSubCategoryImple subSubSubCategoryImple, ProductRepository productRepository) {
        this.subCategoryRepository = subCategoryRepository;
        this.modelMapper = modelMapper;
        this.subSubSubCategoryImple = subSubSubCategoryImple;
        this.productRepository = productRepository;
    }

    @Override
    public Response createSubCategory(SubCategoryDto subCategoryDto) {
        Optional<SubCategory> subCategoryOptional = subCategoryRepository.findByNameAndIsActiveTrue(subCategoryDto.getName());
        if (subCategoryOptional.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.FOUND, "Already have a subCategory With this name");
        }
        try {
            SubCategory subCategory = modelMapper.map(subCategoryOptional.get(), SubCategory.class);
            subCategory.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
             subCategory = subCategoryRepository.save(subCategory);
            if (subCategory != null) {
                if (subCategory.getProductList().isEmpty()) {
                    return ResponseBuilder.getSuccessResponse(HttpStatus.CREATED, "SubCategory Creation Successfully", subCategory.getName());
                }
                List<CreateProductDto> createProductDtoList = new ArrayList<>();
                subCategory.getProductList().forEach(product -> {
                    CreateProductDto createProductDto = new CreateProductDto(product.getId(), Product.class.getSimpleName());
                    createProductDtoList.add(createProductDto);
                });
                return ResponseBuilder.getSuccessResponse(HttpStatus.CREATED, "SubCategory Creation Successfully", createProductDtoList);
            }
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public Response updateSubCategory(Long id, SubCategoryDto subCategoryDto) {
        Optional<SubCategory> subCategoryOptional = subCategoryRepository.findByIdAndIsActiveTrue(id);
        if (!subCategoryOptional.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, "didn't found any SubCategory");
        }
        try {
            SubCategory subCategory = subCategoryOptional.get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(subCategoryDto, SubCategory.class);
            subCategory.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            subCategory = subCategoryRepository.save(subCategory);
            if (subCategory != null) {
                return ResponseBuilder.getSuccessResponse(HttpStatus.OK, "SubCategory Successfully updated", subCategory.getName());
            }
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @Override
    public Response getAllSubCategory() {
        Optional<List<SubCategory>> optionalSubCategoryList = subCategoryRepository.findAllByIsActiveTrue();
        if (optionalSubCategoryList.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, "didn't find any Subcategory");
        }
        List<SubCategoryDto> subCategoryDtoList = this.getAllSubCategoryDto(optionalSubCategoryList.get());
        int numberOfRow = subCategoryRepository.countAllByIsActiveTrue();
        if (!subCategoryDtoList.isEmpty()) {
            return ResponseBuilder.getSuccessResponse(HttpStatus.OK, "SubCategory Retrieved Successfully", subCategoryDtoList, numberOfRow);
        }
        return ResponseBuilder.getSuccessResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", null);
    }

    @Override
    public Response getProductsBySubCategoryId(Long subCategoryId) {
        Optional<SubCategory> subCategoryOptional = subCategoryRepository.findByIdAndIsActiveTrue(subCategoryId);
        if (!subCategoryOptional.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, "didn't find any SubCategory ");
        }
        try {
            SubCategory subCategory = subCategoryOptional.get();
            List<Product> productList = subCategory.getProductList();
            List<ProductDto> productDtoList = subSubSubCategoryImple.getProductList(productList);
            if (productDtoList.isEmpty()) {
                return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, "didn't get any product with this subCategory");
            }
            int numberOfRow = productRepository.countAllByIsActiveTrue();
            return ResponseBuilder.getSuccessResponse(HttpStatus.OK, "Product Retrieved Successfully", productDtoList, numberOfRow);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    private List<SubCategoryDto> getAllSubCategoryDto(List<SubCategory> subCategories) {
        List<SubCategoryDto> subCategoryDtoList = new ArrayList<>();
        subCategories.forEach(subCategory -> {
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            SubCategoryDto subCategoryDto = modelMapper.map(subCategory, SubCategoryDto.class);
            if (!subCategoryDto.getProductList().isEmpty()) {
                /**
                 * For set each product document list if have any product with this subCategory
                 */
                subCategoryDto.setProductList(subSubSubCategoryImple.getProductList(subCategory.getProductList()));
            }
            subCategoryDtoList.add(subCategoryDto);
        });
        return subCategoryDtoList;
    }

}
