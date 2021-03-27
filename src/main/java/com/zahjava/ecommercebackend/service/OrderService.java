package com.zahjava.ecommercebackend.service;

import com.zahjava.ecommercebackend.dto.OrderDto;
import com.zahjava.ecommercebackend.view.Response;

public interface OrderService {
    Response saveOrder(OrderDto orderDto);

    Response updateOrder(Long orderId, OrderDto orderDto);

    Response delOrder(Long orderId);

    Response getAllOrders();

    Response getOrderByOrderId(Long orderId);
}
