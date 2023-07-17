package com.excelsisproject.productservice.service.implementation;

import com.excelsisproject.productservice.dto.OrderDto;

import com.excelsisproject.productservice.entity.Order;
import com.excelsisproject.productservice.exception.ResourceNotFoundException;
import com.excelsisproject.productservice.mapper.OrderMapper;
import com.excelsisproject.productservice.repository.OrderRepository;
import com.excelsisproject.productservice.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {


    private OrderRepository orderRepository;
    @Override
    public OrderDto placeOrder(OrderDto orderDto) {
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


}
