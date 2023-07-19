package com.excelsisproject.productservice.service.implementation;

import com.excelsisproject.productservice.dto.OrderDto;

import com.excelsisproject.productservice.entity.Order;
import com.excelsisproject.productservice.entity.Product;
import com.excelsisproject.productservice.exception.ResourceNotFoundException;
import com.excelsisproject.productservice.mapper.OrderMapper;
import com.excelsisproject.productservice.mapper.ProductMapper;
import com.excelsisproject.productservice.repository.OrderRepository;
import com.excelsisproject.productservice.service.OrderService;
import com.excelsisproject.productservice.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {


    private OrderRepository orderRepository;
    @Override
    public OrderDto orderProduct(OrderDto orderDto) {
        Order order = OrderMapper.mapToOrder(orderDto);
        Order savedOrder = orderRepository.save(order);

        return OrderMapper.mapToOrderDto(savedOrder);
    }

    @Override
    public OrderDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order does not exist by given id: " + orderId));

        return OrderMapper.mapToOrderDto(order);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map((order) -> OrderMapper.mapToOrderDto(order))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto updateOrder(Long orderId, OrderDto updatedOrder) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                ()-> new ResourceNotFoundException("order does not exist with given id: " + orderId));

        order.setOrderUserName(updatedOrder.getOrderUserName());
        order.setOrderUserAddress(updatedOrder.getOrderUserAddress());
        order.setOrderContact(updatedOrder.getOrderContact());
        order.setCartItems(updatedOrder.getCartItems());

        Order updatedOrderObj = orderRepository.save(order);

        return OrderMapper.mapToOrderDto(updatedOrderObj);
    }

    @Override
    public OrderDto updatePrice(Long orderId, double totalPrice) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                ()-> new ResourceNotFoundException("Product does not exist with given id: " + orderId));

        order.setTotalPrice(totalPrice);

        Order updatedOrderObj = orderRepository.save(order);

        return OrderMapper.mapToOrderDto(updatedOrderObj);
    }


}
