package com.zahjava.ecommercebackend.service.impl;

import com.zahjava.ecommercebackend.dto.CreateProductDto;
import com.zahjava.ecommercebackend.dto.DocumentDto;
import com.zahjava.ecommercebackend.dto.ProductDto;
import com.zahjava.ecommercebackend.model.Product;
import com.zahjava.ecommercebackend.repository.ProductRepository;
import com.zahjava.ecommercebackend.service.DocumentService;
import com.zahjava.ecommercebackend.service.ProductService;
import com.zahjava.ecommercebackend.view.Response;
import com.zahjava.ecommercebackend.view.ResponseBuilder;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("ProductService")
public class ProductServiceImpl implements ProductService {
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;
    private final DocumentService documentService;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, DocumentService documentService, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.documentService = documentService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Response createProduct(ProductDto productDto) {
        if (productRepository.findByNameAndIsActiveTrue(productDto.getName()).isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.MULTIPLE_CHOICES, "Already have a product with this name");
        }
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        Product product = modelMapper.map(productDto, Product.class);
        product = productRepository.save(product);
        if (product != null) {
            return ResponseBuilder.getSuccessResponse(HttpStatus.CREATED, "Product Creation Successfully", new CreateProductDto(product.getId(),Product.class.getSimpleName()));
        }
        return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

    @Override
    public Response updateProduct(Long productId, ProductDto productDto) {
        Optional<Product> optionalProduct = productRepository.findByIdAndIsActiveTrue(productId);
        if (!optionalProduct.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, "Product Not Found");
        }
        try {
            Product product = optionalProduct.get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(productDto, product);
            product = productRepository.save(product);
            if (product != null) {
                return ResponseBuilder.getSuccessResponse(HttpStatus.OK, "Product Updated Successfully", product.getName());
            }
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Error Occurred");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public Response getProduct(Long productId) {
        Optional<Product> productOptional = productRepository.findByIdAndIsActiveTrue(productId);
        if (!productOptional.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, "didn't find any product");
        }
        try {
            ProductDto productDto = modelMapper.map(productOptional, ProductDto.class);
            if (productDto != null) {
                List<DocumentDto> documentList = documentService.getAllDtoByDomain(productId, Product.class.getSimpleName());
                productDto.setDocumentList(documentList);
                return ResponseBuilder.getSuccessResponse(HttpStatus.OK, "Product retrieved Successfully", productDto);
            }
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Error Occurred");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public Response deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findByIdAndIsActiveTrue(id);
        if (!optionalProduct.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, "didn't find any product");
        }
        try {
            Product product = optionalProduct.get();
            product.setIsActive(false);
            product.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            product = productRepository.save(product);
            if (product != null) {
                return ResponseBuilder.getSuccessResponse(HttpStatus.OK, "Product Deleted", null);
            }
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Error Occurred");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
