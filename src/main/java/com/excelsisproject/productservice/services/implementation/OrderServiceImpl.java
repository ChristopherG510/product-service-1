package com.excelsisproject.productservice.services.implementation;

import com.excelsisproject.productservice.dto.OrderDto;
import com.excelsisproject.productservice.dto.PageRequestDto;
import com.excelsisproject.productservice.dto.RequestDto;
import com.excelsisproject.productservice.entities.CartItem;
import com.excelsisproject.productservice.entities.Order;
import com.excelsisproject.productservice.entities.User;
import com.excelsisproject.productservice.exceptions.ResourceNotFoundException;
import com.excelsisproject.productservice.mappers.OrderMapper;
import com.excelsisproject.productservice.repositories.CartRepository;
import com.excelsisproject.productservice.repositories.OrderRepository;
import com.excelsisproject.productservice.repositories.UserRepository;
import com.excelsisproject.productservice.services.FilterSpecification;
import com.excelsisproject.productservice.services.OrderService;
import com.excelsisproject.productservice.services.ProductService;
import com.excelsisproject.productservice.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private ProductService productService;
    private UserService userService;
    private UserRepository userRepository;
    private CartRepository cartRepository;
    private FilterSpecification<Order> orderFilterSpecification;

    private static final String ORDER_PLACED = "Pendiente";

    @Override
    public OrderDto orderProduct(OrderDto orderDto, String loggedUser) {
        // Agrega todos los productos en carrito a una orden

        List<CartItem> cartItems = cartRepository.findAllByUserIdAndStatus(userService.getLoggedUserId(), "In cart"); // solo trae los cartItems sin comprar a una lista

        if(!cartItems.isEmpty()) {  // Si el carrito esta vacío devuelve excepción
            double totalPrice = 0;
            for (CartItem cartItem : cartItems) {   // itera por los cartItems, va acumulando los precios para el precio total, y cambia el status del cartItem a comprado
                double amountOrdered = cartItem.getAmount();
                Long productId = cartItem.getProductId();
                productService.updateStock(productId, amountOrdered);   // actualiza el stock del producto
                totalPrice += cartItem.getPrice();
                cartItem.setStatus(ORDER_PLACED);
            }

            User user = userRepository.findByLogin(loggedUser)
                    .orElseThrow(() -> new UsernameNotFoundException("El usuario" + loggedUser + "no existe"));
            Long loggedUserId = user.getId();

            // Se genera la informacion de la orden
            orderDto.setUserId(loggedUserId);
            orderDto.setFirstName(user.getFirstName());
            orderDto.setLastName(user.getLastName());
            orderDto.setUserEmail(user.getUserEmail());
            orderDto.setUserPhoneNumber(user.getUserPhoneNumber());
            orderDto.setRuc(user.getRuc());
            orderDto.setTotalPrice(totalPrice);
            orderDto.setDateOrdered(LocalDateTime.now(ZoneId.of("America/Asuncion")).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            orderDto.setTimeOrdered(LocalDateTime.now(ZoneId.of("America/Asuncion")).format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            orderDto.setCartItems(cartItems);
            orderDto.setOrderStatus(ORDER_PLACED);

            Order order = OrderMapper.mapToOrder(orderDto);
            Order savedOrder = orderRepository.save(order);

            return OrderMapper.mapToOrderDto(savedOrder);
        } else {
            throw new ResourceNotFoundException("Cart is empty");
        }
    }

    @Override
    public OrderDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no existe con id: " + orderId));
        return OrderMapper.mapToOrderDto(order);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(OrderMapper::mapToOrderDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrdersByUser() {
        List<Order> orders = orderRepository.findByUserId(userService.getLoggedUserId());
        return orders.stream().map(OrderMapper::mapToOrderDto).collect(Collectors.toList());
    }

    @Override
    public OrderDto updateOrder(Long orderId, OrderDto updatedOrder) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                ()-> new ResourceNotFoundException("Orden no existe con id: " + orderId));

        order.setOrderStatus(updatedOrder.getOrderStatus());

        Order updatedOrderObj = orderRepository.save(order);
        return OrderMapper.mapToOrderDto(updatedOrderObj);
    }

    @Override
    public void deleteOrder(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(
                ()-> new ResourceNotFoundException("Orden no existe con id: " + orderId));

        orderRepository.deleteById(orderId);
    }

    @Override
    public OrderDto updatePrice(Long orderId, double totalPrice) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                ()-> new ResourceNotFoundException("Orden no existe con id: " + orderId));

        order.setTotalPrice(totalPrice);

        Order updatedOrderObj = orderRepository.save(order);

        return OrderMapper.mapToOrderDto(updatedOrderObj);
    }

    @Override
    public OrderDto updateStatus(Long orderId, String orderStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                ()-> new ResourceNotFoundException("Orden no existe con id: " + orderId));

        order.setOrderStatus(orderStatus);

        Order updatedOrderObj = orderRepository.save(order);

        return OrderMapper.mapToOrderDto(updatedOrderObj);
    }

    @Override
    public List<OrderDto> findByDate(String dateOrdered) {
        List<Order> orders = orderRepository.findByDateOrdered(LocalDate.parse(dateOrdered, DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        List<OrderDto> ordersDto = new ArrayList<>();

        for (Order orderList : orders){
            ordersDto.add(OrderMapper.mapToOrderDto(orderList));
        }

        return ordersDto;
    }

    @Override
    public List<OrderDto> sortOrders(String filter, String direction, int pageNumber, int pageSize) {
        Pageable pageable;
        if(filter == null){
            filter = "id";
        }

        if (Objects.equals(direction, "desc")){
            pageable = PageRequest.of(pageNumber,pageSize, Sort.by(filter).descending());
        } else {
            pageable = PageRequest.of(pageNumber,pageSize, Sort.by(filter).ascending());
        }
        Page<Order> orders;
        if (Objects.equals(filter, "orderStatus")){
            orders = orderRepository.findAllByOrderStatus(filter, pageable);
        } else {
            orders = orderRepository.findAll(pageable);
        }

        return orders.stream().map(OrderMapper::mapToOrderDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> orderFilter(RequestDto requestDto) {
        Specification<Order> searchSpecification = orderFilterSpecification
                .getSearchSpecification(requestDto.getSearchRequestDto(), requestDto.getGlobalOperator());



        Pageable pageable = new PageRequestDto().getPageable(requestDto.getPageDto());

        Page<Order> orders = orderRepository.findAll(searchSpecification, pageable);
        return orders.stream().map(OrderMapper::mapToOrderDto)
                .collect(Collectors.toList());
    }
}