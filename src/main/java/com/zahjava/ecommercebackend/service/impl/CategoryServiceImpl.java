package com.zahjava.ecommercebackend.service.impl;

import com.zahjava.ecommercebackend.dto.CreateProductDto;
import com.zahjava.ecommercebackend.dto.DocumentDto;
import com.zahjava.ecommercebackend.dto.ProductDto;
import com.zahjava.ecommercebackend.dto.categoryDto.CategoryDto;
import com.zahjava.ecommercebackend.model.Product;
import com.zahjava.ecommercebackend.model.categoryModel.Category;
import com.zahjava.ecommercebackend.repository.CategoryRepository;
import com.zahjava.ecommercebackend.repository.ProductRepository;
import com.zahjava.ecommercebackend.service.CategoryService;
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
import java.util.List;
import java.util.Optional;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {
    private final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final DocumentService documentService;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper, ProductRepository productRepository, DocumentService documentService) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
        this.documentService = documentService;
    }

    @Override
    public Response createCategory(CategoryDto categoryDto) {
        Optional<Category> categoryOptional = categoryRepository.findByNameAndIsActiveTrue(categoryDto.getName());
        if (categoryOptional.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.FOUND, "Already have a category with this name");
        }
        try {
            Category category = modelMapper.map(categoryDto, Category.class);
            category.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            category = categoryRepository.save(category);
            if (category != null) {
                if (category.getProductList() == null) {
                    return ResponseBuilder.getSuccessResponse(HttpStatus.CREATED, "Category Creation Successfully", category.getName());
                }
                List<CreateProductDto> createProductDtoList = new ArrayList<>();
                category.getProductList().forEach(product -> {
                    CreateProductDto createProductDto = new CreateProductDto(product.getId(), Category.class.getSimpleName());
                    createProductDtoList.add(createProductDto);
                });
                return ResponseBuilder.getSuccessResponse(HttpStatus.CREATED, "Category Creation Successfully", createProductDtoList);
            }
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @Override
    public Response updateCategory(Long categoryId, CategoryDto categoryDto) {
        Optional<Category> categoryOptional = categoryRepository.findByIdAndIsActiveTrue(categoryId);
        if (!categoryOptional.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, "didn't find any category");
        }
        try {
            Category category = categoryOptional.get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(categoryDto, category);
            category.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            category = categoryRepository.save(category);
            if (category != null) {
                return ResponseBuilder.getSuccessResponse(HttpStatus.OK, "Category Successfully updated", category.getName());
            }
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @Override
    public Response getAllCategory() {
        Optional<List<Category>> optionalCategoryList = categoryRepository.findAllByIsActiveTrue();
        if (!optionalCategoryList.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, "didn't find any category");
        }
        try {
            List<CategoryDto> categoryDtoList = this.getAllCategoryDtos(optionalCategoryList.get());
            int numberOfRow = categoryRepository.countAllByIsActiveTrue();
            if (categoryDtoList.isEmpty()) {
                return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, "didn't found any category");
            }
            return ResponseBuilder.getSuccessResponse(HttpStatus.OK, "Category Retrieved Successfully", categoryDtoList, numberOfRow);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseBuilder.getSuccessResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", null);
        }
    }

    @Override
    public Response getProductsByCategoryId(Long categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findByIdAndIsActiveTrue(categoryId);
        if (!categoryOptional.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, "didn't find any category");
        }
        try {
            Category category = categoryOptional.get();
            List<Product> productList = category.getProductList();
            List<ProductDto> productDtoList = this.getProductList(productList);
            if (productDtoList.isEmpty()) {
                return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, "didn't get any product with this Category");
            }
            int numberOfRow = productRepository.countAllByIsActiveTrue();
            return ResponseBuilder.getSuccessResponse(HttpStatus.OK, "Product Retrieved Successfully", productDtoList, numberOfRow);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseBuilder.getSuccessResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", null);
        }
    }

    private List<CategoryDto> getAllCategoryDtos(List<Category> categories) {
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        categories.forEach(category -> {
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
            if (!categoryDto.getProductList().isEmpty()) {
                /**
                 * For set each product document list if have any product with this Category
                 */
                categoryDto.setProductList(this.getProductList(category.getProductList()));
            }
            categoryDtoList.add(categoryDto);
        });
        return categoryDtoList;
    }

    public List<ProductDto> getProductList(List<Product> productList) {
        List<ProductDto> productDtoList = new ArrayList<>();
        productList.forEach(product -> {
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            ProductDto productDto = modelMapper.map(product, ProductDto.class);
            List<DocumentDto> documentList = documentService.getAllDtoByDomain(productDto.getId(), Category.class.getSimpleName());//for get image for a product by needed param
            productDto.setDocumentList(documentList);
            productDtoList.add(productDto);
        });
        return productDtoList;
    }

}
