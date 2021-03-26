package com.zahjava.ecommercebackend.service.impl;

import com.zahjava.ecommercebackend.dto.OrderDto;
import com.zahjava.ecommercebackend.model.Order;
import com.zahjava.ecommercebackend.repository.OrderRepository;
import com.zahjava.ecommercebackend.service.OrderService;
import com.zahjava.ecommercebackend.view.Response;
import com.zahjava.ecommercebackend.view.ResponseBuilder;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private final String Root = "Order";
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Response saveOrder(OrderDto orderDto) {
        Order order;
        order = modelMapper.map(orderDto, Order.class);
        order.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        order = orderRepository.save(order);
        if (order != null) {
            return ResponseBuilder.getSuccessResponse(HttpStatus.CREATED, Root + "HAS BEAN CREATED", null);
        }
        return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR");
    }

    @Override
    public Response updateOrder(Long orderId, OrderDto orderDto) {
        Order order;
        Optional<Order> orderOptional = orderRepository.findByIdAndIsActiveTrue(orderId);
        if (orderOptional.isPresent()) {
            order = modelMapper.map(orderDto, Order.class);
            order.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            order = orderRepository.save(order);
            if (order != null) {
                return ResponseBuilder.getSuccessResponse(HttpStatus.OK, Root + "has been Updated", null);
            }
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
        return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, Root + "is not Found");
    }

    @Override
    public Response delOrder(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findByIdAndIsActiveTrue(orderId);
        Order order = orderOptional.get();
        if (orderOptional.isPresent()) {
            order.setIsActive(false);
            order.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            order = orderRepository.save(order);
            if (order != null) {
                return ResponseBuilder.getSuccessResponse(HttpStatus.OK, Root + "Delete SucessFully", null);
            }
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
        return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, Root + "NOT FOUND");
    }

    @Override
    public Response getAllOrders() {
        Optional<List<Order>> optionalOrderList = orderRepository.findAllByIsActiveTrue();
        if (optionalOrderList.isPresent()) {
            List<OrderDto> orderDtoList = this.getOrders(optionalOrderList.get());
            return ResponseBuilder.getSuccessResponse(HttpStatus.OK, Root + "Data Retrieve Successfully", orderDtoList);
        }
        return ResponseBuilder.getSuccessResponse(HttpStatus.OK, "There is No" + Root, null);
    }

    @Override
    public Response getOrderByOrderId(Long orderId) {
        Order order;
        Optional<Order> orderOptional = orderRepository.findByIdAndIsActiveTrue(orderId);
        order = orderOptional.get();
        if (orderOptional.isPresent()) {
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            OrderDto orderDto = modelMapper.map(order, OrderDto.class);
            return ResponseBuilder.getSuccessResponse(HttpStatus.OK, Root + "Data Retrieve Successfully", orderDto);
        }
        return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, Root + "NOT FOUND");
    }

    private List<OrderDto> getOrders(List<Order> orders) {
        List<OrderDto> orderDtoList = new ArrayList<>();
        orders.forEach(order -> {
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            OrderDto orderDto = modelMapper.map(order, OrderDto.class);
            orderDtoList.add(orderDto);
        });
        return orderDtoList;
    }
}
