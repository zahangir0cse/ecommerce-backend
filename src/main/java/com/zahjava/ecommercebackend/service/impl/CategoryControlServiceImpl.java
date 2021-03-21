package com.zahjava.ecommercebackend.service.impl;

import com.zahjava.ecommercebackend.dto.CategoryCommonDto;
import com.zahjava.ecommercebackend.model.Item;
import com.zahjava.ecommercebackend.model.ItemRelation;
import com.zahjava.ecommercebackend.repository.ItemRelationHelperRepository;
import com.zahjava.ecommercebackend.repository.ItemsRepository;
import com.zahjava.ecommercebackend.service.CategoryControlService;
import com.zahjava.ecommercebackend.view.Response;
import com.zahjava.ecommercebackend.view.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("categoryControlService")
public class CategoryControlServiceImpl implements CategoryControlService {

    private final ItemRelationHelperRepository itemRelationRepository;
    private final ItemsRepository itemsRepository;
    private final Logger logger = LoggerFactory.getLogger(CategoryControlServiceImpl.class.getName());

    public CategoryControlServiceImpl(ItemRelationHelperRepository itemRelationRepository, ItemsRepository itemsRepository) {
        this.itemRelationRepository = itemRelationRepository;
        this.itemsRepository = itemsRepository;
    }


    @Override
    public Response createCategoryNew(CategoryCommonDto categoryCommonDto) {


        Optional<Item> itemOptional = itemsRepository.findByNameAndIsProductFalse(categoryCommonDto.getName());//here product is false means this is category
        if (itemOptional.isPresent()) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_ACCEPTABLE, "Already have a product with this name ");
        }
        try {
            Item item = new Item();
            item.setName(categoryCommonDto.getName());
            item.setDescription(categoryCommonDto.getDescription());
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
}
