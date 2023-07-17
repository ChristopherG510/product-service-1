package com.excelsisproject.productservice.mapper;

import com.excelsisproject.productservice.dto.OrderDto;
import com.excelsisproject.productservice.entity.Order;

public class OrderMapper {
    public static OrderDto mapToOrderDto(Order order){
        return new OrderDto(
                order.getOrderId(),
                order.getOrderUserName(),
                order.getOrderUserAddress(),
                order.getOrderContact(),
                order.getProductId(),
                order.getOrderAmount()
        );
    }

    public static Order mapToOrder(OrderDto orderDto){
        return new Order(
                orderDto.getOrderId(),
                orderDto.getOrderUserName(),
                orderDto.getOrderUserAddress(),
                orderDto.getOrderContact(),
                orderDto.getProductId(),
                orderDto.getOrderAmount()
        );
    }
}
