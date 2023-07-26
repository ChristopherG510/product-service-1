package com.excelsisproject.productservice.controllers;

import com.excelsisproject.productservice.dto.OrderDto;
import com.excelsisproject.productservice.filters.JwtAuthenticationFilter;
import com.excelsisproject.productservice.services.OrderService;
import com.excelsisproject.productservice.services.ProductService;
import io.jsonwebtoken.Jwt;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.security.SecurityPermission;
import java.util.List;


@AllArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;
    private ProductService productService;
    private ClosingDetailsController detailsController;

    // create order
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    @PostMapping
    public OrderDto orderProduct(@RequestBody OrderDto orderDto){
        return orderService.orderProduct(orderDto);
    }

    // get order
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    @GetMapping("{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable("id") Long orderId){
        OrderDto orderDto = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderDto);
    }

    // get all orders
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders(){
        List<OrderDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Update order
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable("id") Long orderId, @RequestBody OrderDto updatedOrder){
        OrderDto orderDto = orderService.updateOrder(orderId, updatedOrder);
        return ResponseEntity.ok(orderDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") Long orderId){
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok("Order deleted.");
    }
}
