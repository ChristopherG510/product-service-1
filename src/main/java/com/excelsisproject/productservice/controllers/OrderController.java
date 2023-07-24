package com.excelsisproject.productservice.controllers;

import com.excelsisproject.productservice.dto.OrderDto;
import com.excelsisproject.productservice.entities.Cart;
import com.excelsisproject.productservice.services.OrderService;
import com.excelsisproject.productservice.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;
    private ProductService productService;
    private ClosingDetailsController detailsController;

    // create order
    @PostMapping
    public ResponseEntity<OrderDto> orderProduct(@RequestBody OrderDto orderDto){
        List<Cart> cartItems = orderDto.getCartItems();
        double totalPrice = 0;
        for (Cart cart : cartItems){
            double amountOrdered = cart.getAmount();
            Long productId = cart.getProductId();
            productService.updateStock(productId, amountOrdered);
            cart.setPrice(productService.getPrice(productId));
            totalPrice += cart.getPrice() * cart.getAmount();
        }
        orderDto.setTotalPrice(totalPrice);
        orderDto.setDateOrdered(LocalDateTime.now(ZoneId.of("America/Asuncion")).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        orderDto.setTimeOrdered(LocalDateTime.now(ZoneId.of("America/Asuncion")).format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        OrderDto savedOrder = orderService.orderProduct(orderDto);

        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    // get order
    @GetMapping("{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable("id") Long orderId){
        OrderDto orderDto = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderDto);
    }

    // get all orders
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders(){
        List<OrderDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Update order
    @PutMapping("{id}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable("id") Long orderId, @RequestBody OrderDto updatedOrder){
        OrderDto orderDto = orderService.updateOrder(orderId, updatedOrder);
        return ResponseEntity.ok(orderDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") Long orderId){
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok("Order deleted.");
    }
}
