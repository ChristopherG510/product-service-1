package com.excelsisproject.productservice.mappers;

import com.excelsisproject.productservice.dto.OrderDto;
import com.excelsisproject.productservice.entities.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class OrderMapper {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static OrderDto mapToOrderDto(Order order){
        return new OrderDto(
                order.getOrderId(),
                order.getUserId(),
                order.getFirstName(),
                order.getLastName(),
                order.getUserEmail(),
                order.getUserPhoneNumber(),
                order.getUserAddress(),
                order.getRuc(),
                order.getPaymentMethod(),
                order.getOrderDescription(),
                order.getOrderStatus(),
                order.getDateOrdered().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                order.getTimeOrdered().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
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
                orderDto.getRuc(),
                orderDto.getPaymentMethod(),
                orderDto.getOrderDescription(),
                orderDto.getOrderStatus(),
                LocalDate.parse(orderDto.getDateOrdered(), DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                LocalTime.parse(orderDto.getTimeOrdered(), DateTimeFormatter.ofPattern("HH:mm:ss")),
                orderDto.getCartItems(),
                orderDto.getTotalPrice()
        );
    }
}
