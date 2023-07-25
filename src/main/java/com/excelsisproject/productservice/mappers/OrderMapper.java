package com.excelsisproject.productservice.mappers;

import com.excelsisproject.productservice.dto.OrderDto;
import com.excelsisproject.productservice.entities.Order;

public class OrderMapper {
    public static OrderDto mapToOrderDto(Order order){
        return new OrderDto(
                order.getOrderId(),
                order.getUserId(),
                order.getDateOrdered(),
                order.getTimeOrdered(),
                order.getCartItems(),
                order.getTotalPrice()
        );
    }


    public static Order mapToOrder(OrderDto orderDto){
        return new Order(
                orderDto.getOrderId(),
                orderDto.getUserId(),
                orderDto.getDateOrdered(),
                orderDto.getTimeOrdered(),
                orderDto.getCartItems(),
                orderDto.getTotalPrice()
        );
    }
}
