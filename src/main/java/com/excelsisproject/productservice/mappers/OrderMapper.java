package com.excelsisproject.productservice.mappers;

import com.excelsisproject.productservice.dto.OrderDto;
import com.excelsisproject.productservice.entities.Order;

public class OrderMapper {
    public static OrderDto mapToOrderDto(Order order){
        return new OrderDto(
                order.getOrderId(),
                order.getOrderUserName(),
                order.getOrderUserAddress(),
                order.getOrderContact(),
                order.getDateOrdered(),
                order.getTimeOrdered(),
                order.getCartItems(),
                order.getTotalPrice()
        );
    }


    public static Order mapToOrder(OrderDto orderDto){
        return new Order(
                orderDto.getOrderId(),
                orderDto.getOrderUserName(),
                orderDto.getOrderUserAddress(),
                orderDto.getOrderContact(),
                orderDto.getDateOrdered(),
                orderDto.getTimeOrdered(),
                orderDto.getCartItems(),
                orderDto.getTotalPrice()
        );
    }
}
