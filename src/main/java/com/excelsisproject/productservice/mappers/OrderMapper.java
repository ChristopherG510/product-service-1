package com.excelsisproject.productservice.mappers;

import com.excelsisproject.productservice.dto.OrderDto;
import com.excelsisproject.productservice.entities.Order;

public class OrderMapper {
    public static OrderDto mapToOrderDto(Order order){
        return new OrderDto(
                order.getOrderId(),
                order.getUserId(),
                order.getFirstName(),
                order.getLastName(),
                order.getUserEmail(),
                order.getUserPhoneNumber(),
                order.getUserAddress(),
                order.getPaymentMethod(),
                order.getOrderDescription(),
                order.getOrderStatus(),
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
                orderDto.getFirstName(),
                orderDto.getLastName(),
                orderDto.getUserEmail(),
                orderDto.getUserPhoneNumber(),
                orderDto.getUserAddress(),
                orderDto.getPaymentMethod(),
                orderDto.getOrderDescription(),
                orderDto.getOrderStatus(),
                orderDto.getDateOrdered(),
                orderDto.getTimeOrdered(),
                orderDto.getCartItems(),
                orderDto.getTotalPrice()
        );
    }
}
