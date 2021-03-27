package com.zahjava.ecommercebackend.controller;

import com.zahjava.ecommercebackend.annotation.ApiController;
import com.zahjava.ecommercebackend.dto.OrderDto;
import com.zahjava.ecommercebackend.service.OrderService;
import com.zahjava.ecommercebackend.utils.UrlConstraint;
import com.zahjava.ecommercebackend.view.Response;
import org.springframework.web.bind.annotation.*;

@ApiController
@RequestMapping(UrlConstraint.OrderManagement.ROOT)
public class OrderController {
    private final OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Response saveOrder(@RequestBody OrderDto orderDto) {
        return orderService.saveOrder(orderDto);
    }

    @PutMapping(UrlConstraint.OrderManagement.PUT)
    public Response updateOrder(@PathVariable("orderId") Long orderId, @RequestBody OrderDto orderDto) {
        return orderService.updateOrder(orderId, orderDto);
    }

    @GetMapping
    public Response orderList() {
        return orderService.getAllOrders();
    }

    @GetMapping(UrlConstraint.OrderManagement.GET)
    public Response getOrderById(@PathVariable("orderId") Long orderId) {
        return orderService.getOrderByOrderId(orderId);
    }

    @DeleteMapping(UrlConstraint.OrderManagement.DELETE)
    public Response delOrder(@PathVariable("orderId") Long orderId) {
        return orderService.delOrder(orderId);
    }
}
