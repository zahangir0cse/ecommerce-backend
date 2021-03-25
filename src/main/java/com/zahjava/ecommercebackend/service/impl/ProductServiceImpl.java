package com.zahjava.ecommercebackend.service.impl;

import com.zahjava.ecommercebackend.dto.CreateProductDto;
import com.zahjava.ecommercebackend.dto.DocumentDto;
import com.zahjava.ecommercebackend.dto.ItemDto;
import com.zahjava.ecommercebackend.model.Item;
import com.zahjava.ecommercebackend.repository.ItemsRepository;
import com.zahjava.ecommercebackend.service.DocumentService;
import com.zahjava.ecommercebackend.service.ProductService;
import com.zahjava.ecommercebackend.view.Response;
import com.zahjava.ecommercebackend.view.ResponseBuilder;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("productService")
public class ProductServiceImpl implements ProductService {
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final DocumentService documentService;
    private final ItemsRepository itemsRepository;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(DocumentService documentService, ItemsRepository itemsRepository, ModelMapper modelMapper) {
        this.documentService = documentService;
        this.itemsRepository = itemsRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Response createProduct(ItemDto itemDto) {
        Optional<Item> itemOptional = itemsRepository.findByNameAndIsProductTrueAndIsActiveTrue(itemDto.getName());//here product is false means this is category
        if (itemOptional.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_ACCEPTABLE, "Already have a product with this name ");
        }
        try {
            Item item = modelMapper.map(itemDto, Item.class);
            item.setIsProduct(true);
            item = itemsRepository.save(item);
            if (item != null) {
                return ResponseBuilder.getSuccessResponse(HttpStatus.CREATED, "Product Creation Successfully", new CreateProductDto(item.getId(), Item.class.getSimpleName()));
            }
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @Override
    public Response getAllProducts() {
        Optional<List<Item>> optionalItemList = itemsRepository.findAllByIsActiveTrueAndIsProductTrue();
        if (!optionalItemList.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, "didn't find any product");
        }
        try {
            return ResponseBuilder.getSuccessResponse(HttpStatus.FOUND, "Product Retrieved Successfully", getItemList(optionalItemList.get()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @Override
    public Response getProductById(Long id) {
        Optional<Item> itemOptional = itemsRepository.findByIdAndIsActiveTrueAndIsProductTrue(id);
        if (!itemOptional.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, "didn't find any product");
        }
        try {
            Item item = itemOptional.get();
            List<DocumentDto> documentDtoList = documentService.getAllDtoByDomain(item.getId(), Item.class.getSimpleName());
            ItemDto itemDto = modelMapper.map(item, ItemDto.class);
            itemDto.setDocumentList(documentDtoList);
            return ResponseBuilder.getSuccessResponse(HttpStatus.FOUND, "Product Retrieved Successfully", itemDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    public List<ItemDto> getItemList(List<Item> itemList) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        itemList.forEach(item -> {
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            ItemDto itemDto = modelMapper.map(item, ItemDto.class);
            List<DocumentDto> documentList = documentService.getAllDtoByDomain(itemDto.getId(), Item.class.getSimpleName());
            itemDto.setDocumentList(documentList);
            itemDtoList.add(itemDto);
        });
        return itemDtoList;
    }
}
