package com.excelsisproject.productservice.service;

import com.excelsisproject.productservice.dto.OrderDto;
import com.excelsisproject.productservice.dto.ProductDto;

import java.util.List;

public interface OrderService {
    OrderDto placeOrder(OrderDto orderDto);

    OrderDto getOrderById(Long orderId);

    List<OrderDto> getAllOrders();

    OrderDto updateOrder(Long orderId, OrderDto updatedOrder);

}
