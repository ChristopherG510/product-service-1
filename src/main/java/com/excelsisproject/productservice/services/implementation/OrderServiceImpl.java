package com.excelsisproject.productservice.services.implementation;

import com.excelsisproject.productservice.dto.OrderDto;
import com.excelsisproject.productservice.entities.Cart;
import com.excelsisproject.productservice.entities.Order;
import com.excelsisproject.productservice.entities.User;
import com.excelsisproject.productservice.exceptions.ResourceNotFoundException;
import com.excelsisproject.productservice.mappers.OrderMapper;
import com.excelsisproject.productservice.repositories.OrderRepository;
import com.excelsisproject.productservice.repositories.ProductRepository;
import com.excelsisproject.productservice.repositories.UserRepository;
import com.excelsisproject.productservice.services.OrderService;
import com.excelsisproject.productservice.services.ProductService;
import com.excelsisproject.productservice.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {


    private OrderRepository orderRepository;
    private ProductService productService;
    private UserService userService;
    private UserRepository userRepository;
    @Override
    public OrderDto orderProduct(OrderDto orderDto, String loggedUser) {


        List<Cart> cartItems = orderDto.getCartItems();

        double totalPrice = 0;
        for (Cart cart : cartItems){
            double amountOrdered = cart.getAmount();
            Long productId = cart.getProductId();
            productService.updateStock(productId, amountOrdered);
            cart.setPrice(productService.getPrice(productId));
            totalPrice += cart.getPrice() * cart.getAmount();
        }

        User user = userRepository.findByLogin(loggedUser)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario" + loggedUser + "no existe"));
        Long loggedUserId = user.getId();
        System.out.println("id: " + loggedUserId);

        orderDto.setUserId(loggedUserId);



        orderDto.setFirstName(user.getFirstName());
        orderDto.setLastName(user.getLastName());
        orderDto.setUserEmail(user.getUserEmail());
        orderDto.setUserPhoneNumber(user.getUserPhoneNumber());
        orderDto.setUserAddress(user.getUserAddress());
        orderDto.setTotalPrice(totalPrice);
        orderDto.setDateOrdered(LocalDateTime.now(ZoneId.of("America/Asuncion")).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        orderDto.setTimeOrdered(LocalDateTime.now(ZoneId.of("America/Asuncion")).format(DateTimeFormatter.ofPattern("HH:mm:ss")));

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
