package com.excelsisproject.productservice.services.implementation;

import com.excelsisproject.productservice.dto.OrderDto;
import com.excelsisproject.productservice.entities.Cart;
import com.excelsisproject.productservice.entities.Order;
import com.excelsisproject.productservice.exceptions.ResourceNotFoundException;
import com.excelsisproject.productservice.mappers.OrderMapper;
import com.excelsisproject.productservice.repositories.OrderRepository;
import com.excelsisproject.productservice.repositories.ProductRepository;
import com.excelsisproject.productservice.services.OrderService;
import com.excelsisproject.productservice.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {


    private OrderRepository orderRepository;
    private ProductService productService;
    @Override
    public OrderDto orderProduct(OrderDto orderDto) {
        Order order = OrderMapper.mapToOrder(orderDto);

        List<Cart> cartItems = orderDto.getCartItems();

        double totalPrice = 0;
        for (Cart cart : cartItems){
            double amountOrdered = cart.getAmount();
            Long productId = cart.getProductId();
            productService.updateStock(productId, amountOrdered);
            cart.setPrice(productService.getPrice(productId));
            totalPrice += cart.getPrice() * cart.getAmount();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        System.out.println("Usuario Logueado: " + currentPrincipalName);

        //orderDto.setOrderId(Long.parseLong(usuarioLogueado));
        orderDto.setTotalPrice(totalPrice);
        orderDto.setDateOrdered(LocalDateTime.now(ZoneId.of("America/Asuncion")).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        orderDto.setTimeOrdered(LocalDateTime.now(ZoneId.of("America/Asuncion")).format(DateTimeFormatter.ofPattern("HH:mm:ss")));

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


        order.setCartItems(updatedOrder.getCartItems());

        Order updatedOrderObj = orderRepository.save(order);

        return OrderMapper.mapToOrderDto(updatedOrderObj);
    }

    @Override
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                ()-> new ResourceNotFoundException("Order does not exist with given id: " + orderId));

        orderRepository.deleteById(orderId);
    }

    @Override
    public OrderDto updatePrice(Long orderId, double totalPrice) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                ()-> new ResourceNotFoundException("Product does not exist with given id: " + orderId));

        order.setTotalPrice(totalPrice);

        Order updatedOrderObj = orderRepository.save(order);

        return OrderMapper.mapToOrderDto(updatedOrderObj);
    }

    @Override
    public List<OrderDto> findByDate(String dateOrdered) {
        System.out.println(orderRepository.findByDateOrdered(dateOrdered));
        List<Order> orders = orderRepository.findByDateOrdered(dateOrdered);
        List<OrderDto> ordersDto = new ArrayList<>();

        for (Order orderList : orders){
            ordersDto.add(OrderMapper.mapToOrderDto(orderList));
        }

        return ordersDto;
    }
}
