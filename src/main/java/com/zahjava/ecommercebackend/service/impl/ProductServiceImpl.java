package com.zahjava.ecommercebackend.service.impl;

import com.zahjava.ecommercebackend.dto.CreateProductDto;
import com.zahjava.ecommercebackend.dto.DocumentDto;
import com.zahjava.ecommercebackend.dto.ItemDto;
import com.zahjava.ecommercebackend.model.Item;
import com.zahjava.ecommercebackend.model.ItemPurchaseHistory;
import com.zahjava.ecommercebackend.model.ItemSalePriceHistory;
import com.zahjava.ecommercebackend.repository.ItemPurchaseHistoryRepository;
import com.zahjava.ecommercebackend.repository.ItemSalePriceHistoryRepository;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("productService")
public class ProductServiceImpl implements ProductService {
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final DocumentService documentService;
    private final ItemsRepository itemsRepository;
    private final ModelMapper modelMapper;
    private final ItemPurchaseHistoryRepository itemPurchaseHistoryRepository;
    private final ItemSalePriceHistoryRepository itemSalePriceHistoryRepository;

    public ProductServiceImpl(DocumentService documentService, ItemsRepository itemsRepository, ModelMapper modelMapper, ItemPurchaseHistoryRepository itemPurchaseHistoryRepository, ItemSalePriceHistoryRepository itemSalePriceHistoryRepository) {
        this.documentService = documentService;
        this.itemsRepository = itemsRepository;
        this.modelMapper = modelMapper;
        this.itemPurchaseHistoryRepository = itemPurchaseHistoryRepository;
        this.itemSalePriceHistoryRepository = itemSalePriceHistoryRepository;
    }

    @Override
    public Response createProduct(ItemDto itemDto) {
        if (itemDto.getSalePrice() < itemDto.getPurchasePrice()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.CHECKPOINT, "Sale price should be equal or more then Purchase price");
        }
        Optional<Item> itemOptional = itemsRepository.findByNameAndIsProductTrueAndIsActiveTrue(itemDto.getName());//here product is false means this is category
        if (itemOptional.isPresent()) {
            try {
                Item currentItem = itemOptional.get();
                Long updateQuantity = currentItem.getQuantity() + itemDto.getQuantity();
                currentItem.setQuantity(updateQuantity);
                currentItem.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
                currentItem = itemsRepository.save(currentItem);
                if (currentItem != null) {
                    /**
                     * Now set data to purchase history table
                     */
                    Boolean isCompleteSetPurchaseHistory = setItemPurchaseHistory(itemDto, currentItem);
                    if (isCompleteSetPurchaseHistory) {
                        /**
                         * Now set Item Sale Price history for condition {newProduct = false}
                         */
                        Boolean isCompleteSetSalePriceHistory = setItemSalePriceHistory(itemDto, currentItem, false);
                        if (isCompleteSetSalePriceHistory) {
                            return ResponseBuilder.getSuccessResponse(HttpStatus.OK, "Product Updated Successfully", new CreateProductDto(currentItem.getId(), Item.class.getSimpleName()));//send CreateProductDto response for set item image by current save item item and entity name
                        }
                        return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
                    }
                    return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
            }
        }
        /***
         * This is item is new item so save this new item
         */
        Item item = modelMapper.map(itemDto, Item.class);
        item.setIsProduct(true);
        item.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        item = itemsRepository.save(item);
        if (item != null) {
            /**
             * Now set data to purchase history table
             */
            Boolean isCompleteSetPurchaseHistory = setItemPurchaseHistory(itemDto, item);
            if (isCompleteSetPurchaseHistory) {
                /**
                 * Now set Item Sale Price history for condition {newProduct = false}
                 */
                Boolean isCompleteSetSalePriceHistory = setItemSalePriceHistory(itemDto, item, true);
                if (isCompleteSetSalePriceHistory) {
                    return ResponseBuilder.getSuccessResponse(HttpStatus.CREATED, "Product Creation Successfully", new CreateProductDto(item.getId(), Item.class.getSimpleName()));//send CreateProductDto response for set item image by current save item item and entity name
                }
                return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
            }
        }
        return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");

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


    private Boolean setItemPurchaseHistory(ItemDto itemDto, Item currentItem) {
        try {
            ItemPurchaseHistory itemPurchaseHistory = new ItemPurchaseHistory();
            itemPurchaseHistory.setProductId(currentItem.getId());
            itemPurchaseHistory.setPurchaseQuantity(itemDto.getPurchaseQuantity());
            itemPurchaseHistory.setPurchasePrice(itemDto.getPurchasePrice());
            itemPurchaseHistory.setPurchaseCompany(itemDto.getPurchaseCompany());
            itemPurchaseHistory.setAddress(itemDto.getAddress());
            itemPurchaseHistory.setPurchaseDate(new Date());
            itemPurchaseHistory.setPurchaseBy(SecurityContextHolder.getContext().getAuthentication().getName());
            itemPurchaseHistory = itemPurchaseHistoryRepository.save(itemPurchaseHistory);
            if (itemPurchaseHistory != null) {
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("" + e.getMessage());
            return false;
        }
    }
    
    private Boolean setItemSalePriceHistory(ItemDto itemDto, Item currentItem, boolean isNewProduct) {
        Optional<ItemSalePriceHistory> itemSalePriceHistoryOptional = itemSalePriceHistoryRepository.findByProductIdAndIsActivePriceTrueAndIsActiveTrue(currentItem.getId());

        try {
            if (itemSalePriceHistoryOptional.isPresent() && !isNewProduct) {
                ItemSalePriceHistory currentItemSalePriceHistory = itemSalePriceHistoryOptional.get();
                currentItemSalePriceHistory.setEffectiveDate(new Date());
                currentItemSalePriceHistory.setIsActivePrice(false);
                currentItemSalePriceHistory.setEndDate(new Date());
                currentItemSalePriceHistory.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
                currentItemSalePriceHistory = itemSalePriceHistoryRepository.save(currentItemSalePriceHistory);
                if (currentItemSalePriceHistory != null) {
                    /**
                     * Now create  another one raw for updated price
                     */
                    ItemSalePriceHistory newItemSalePriceHistory = new ItemSalePriceHistory();
                    newItemSalePriceHistory.setProductId(currentItem.getId());
                    newItemSalePriceHistory.setSalePrice(itemDto.getSalePrice());
                    newItemSalePriceHistory.setEffectiveDate(new Date());
                    newItemSalePriceHistory.setIsActivePrice(true);
                    newItemSalePriceHistory.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
                    newItemSalePriceHistory = itemSalePriceHistoryRepository.save(newItemSalePriceHistory);
                    if (newItemSalePriceHistory != null) {
                        return true;
                    }
                    return false;
                }
                return false;
            }
            /**
             * This is new product
             */
            ItemSalePriceHistory newItemSalePrice = new ItemSalePriceHistory();
            newItemSalePrice.setProductId(currentItem.getId());
            newItemSalePrice.setSalePrice(itemDto.getSalePrice());
            newItemSalePrice.setEffectiveDate(new Date());
            newItemSalePrice.setIsActivePrice(true);
            newItemSalePrice.setEndDate(null);//for detect a product/item is new or not
            newItemSalePrice.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            newItemSalePrice = itemSalePriceHistoryRepository.save(newItemSalePrice);
            if (newItemSalePrice != null) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
