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
                order.getCartItems(),
                order.getTotalPrice()
        );
    }


    public static Order mapToOrder(OrderDto orderDto){

//        double fullPrice = 0;
//        for (Cart cart : orderDto.getCartItems()){
//            fullPrice += cart.getAmount() * productService.returnPrice(productService.getProductById(cart.getProductId()).getId());
//        }

        return new Order(
                orderDto.getOrderId(),
                orderDto.getOrderUserName(),
                orderDto.getOrderUserAddress(),
                orderDto.getOrderContact(),
                orderDto.getCartItems(),
                orderDto.getTotalPrice()
        );
    }
}
