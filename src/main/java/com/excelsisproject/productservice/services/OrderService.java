package com.excelsisproject.productservice.services;

import com.excelsisproject.productservice.dto.OrderDto;

import java.util.List;

public interface OrderService {
    OrderDto orderProduct(OrderDto orderDto, String loggedUserId);

    OrderDto getOrderById(Long orderId);

    List<OrderDto> getAllOrders();

    OrderDto updateOrder(Long orderId, OrderDto updatedOrder);

    void deleteOrder(Long orderId);

    OrderDto updatePrice(Long orderId, double totalPrice);

    List<OrderDto> findByDate(String dateOrdered);
}
