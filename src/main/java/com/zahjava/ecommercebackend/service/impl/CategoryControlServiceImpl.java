package com.zahjava.ecommercebackend.service.impl;

import com.zahjava.ecommercebackend.dto.CategoryCommonDto;
import com.zahjava.ecommercebackend.dto.ItemDto;
import com.zahjava.ecommercebackend.model.Item;
import com.zahjava.ecommercebackend.model.ItemRelation;
import com.zahjava.ecommercebackend.repository.ItemRelationHelperRepository;
import com.zahjava.ecommercebackend.repository.ItemsRepository;
import com.zahjava.ecommercebackend.service.CategoryControlService;
import com.zahjava.ecommercebackend.service.DocumentService;
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

@Service("categoryControlService")
public class CategoryControlServiceImpl implements CategoryControlService {

    private final ItemRelationHelperRepository itemRelationRepository;
    private final ItemsRepository itemsRepository;
    private final ModelMapper modelMapper;
    private final DocumentService documentService;
    private final Logger logger = LoggerFactory.getLogger(CategoryControlServiceImpl.class.getName());

    public CategoryControlServiceImpl(ItemRelationHelperRepository itemRelationRepository, ItemsRepository itemsRepository, ModelMapper modelMapper, DocumentService documentService) {
        this.itemRelationRepository = itemRelationRepository;
        this.itemsRepository = itemsRepository;
        this.modelMapper = modelMapper;
        this.documentService = documentService;
    }


    @Override
    public Response createCategoryNew(CategoryCommonDto categoryCommonDto) {
        Optional<Item> itemOptional = itemsRepository.findByNameAndIsProductFalseAndIsActiveTrue(categoryCommonDto.getName());//here product is false means this is category
        if (itemOptional.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_ACCEPTABLE, "Already have a Category with this name ");
        }
        /**
         * now check parent if have any parent ID with this request
         *//*
        if (categoryCommonDto.getParentId() != null) {
            itemOptional = itemsRepository.findByIdAndIsProductFalseAndIsActiveTrue(categoryCommonDto.getParentId());
            if (!itemOptional.isPresent()) {
                return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, "You Don't have Category With this Id");
            }
        }*/
        /**
         * all ok now create a category
         */
        try {
            Item item = modelMapper.map(categoryCommonDto, Item.class);
            item.setIsProduct(false);
            item = itemsRepository.save(item);
            if (item != null) {
                /**
                 * if current requested category have any parent category then
                 */
                if (categoryCommonDto.getParentId() != null) {
                    ItemRelation itemRelation = new ItemRelation();
                    itemRelation.setItemId(item.getId());
                    itemRelation.setItemParentId(categoryCommonDto.getParentId());
                    itemRelation = itemRelationRepository.save(itemRelation);
                    if (itemRelation != null) {
                        return ResponseBuilder.getFailureResponse(HttpStatus.CREATED, "Category Creation Successfully With a Parent");
                    }
                }
                /**
                 * here didn't have any parent category so
                 */
                return ResponseBuilder.getFailureResponse(HttpStatus.CREATED, "Category Creation Successfully Without Parent");
            }
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @Override
    public Response getCategory(Long id) {
        Optional<Item> itemOptional = itemsRepository.findByIdAndIsProductFalseAndIsActiveTrue(id);
        if (!itemOptional.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, "didn't find any Category");
        }
        try {
            List<ItemRelation> itemRelationList = itemRelationRepository.findAllByItemParentIdAndIsActiveTrue(id);
            if (!itemRelationList.isEmpty()) {
                return ResponseBuilder.getSuccessResponse(HttpStatus.FOUND, "Items Retrieved Successfully", getAllItems(itemRelationList));
            }
            /**
             * didn't have any relation so this is root category without parent
             */
            return ResponseBuilder.getSuccessResponse(HttpStatus.FOUND, "Items Retrieved Successfully, and this is root items without parent", itemOptional.get());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @Override
    public Response getAllRootCategory() {
        Optional<List<Item>> optionalItemList = itemsRepository.findAllByIsActiveTrueAndIsProductFalse();
        if (!optionalItemList.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, "didnt found any category");
        }
        try {
            /**
             * now get all root category by check relation  with item relation table
             */
            return ResponseBuilder.getSuccessResponse(HttpStatus.FOUND, "Root Category Retrieved Successfully", getAllRootItems(optionalItemList.get()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    /**
     * For get all root categories
     */
    private List<ItemDto> getAllRootItems(List<Item> optionalItemList) {
        List<ItemDto> rootIemDtoList = new ArrayList<>();
        optionalItemList.forEach(item -> {
            Optional<ItemRelation> itemRelationOptional = itemRelationRepository.findByItemParentIdAndIsActiveTrue(item.getId());
            if (!itemRelationOptional.isPresent()) {
                modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
                ItemDto rootIemDto = modelMapper.map(item, ItemDto.class);
                rootIemDtoList.add(rootIemDto);
            }
        });
        return rootIemDtoList;
    }

    /**
     * For get all sub category
     */
    private List<ItemDto> getAllItems(List<ItemRelation> itemRelations) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        itemRelations.forEach(itemRelation -> {
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            Optional<Item> item = itemsRepository.findByIdAndIsActiveTrue(itemRelation.getItemId());
            if (item.isPresent()) {
                ItemDto itemDto = modelMapper.map(item.get(), ItemDto.class);
                /**
                 * now set document if current item is product otherwise this will be empty
                 */
                itemDto.setDocumentList(documentService.getAllDtoByDomain(itemDto.getId(), Item.class.getSimpleName()));
                itemDtoList.add(itemDto);
            }
        });
        return itemDtoList;
    }
}
