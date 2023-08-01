package com.excelsisproject.productservice.controllers;

import com.excelsisproject.productservice.dto.OrderDto;
import com.excelsisproject.productservice.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private OrderService orderService;

    // create order
    // @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    @PostMapping("/order")
    public OrderDto orderProduct(@RequestBody OrderDto orderDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUser = authentication.getName();
        System.out.println("Usuario Logueado: " + loggedUser);
        return orderService.orderProduct(orderDto, loggedUser);
    }

    // get order
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    @GetMapping("/view/orderId/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable("id") Long orderId){
        OrderDto orderDto = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderDto);
    }

    // get all orders
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/viewAll")
    public ResponseEntity<List<OrderDto>> getAllOrders(){
        List<OrderDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Update order
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/edit/orderId/{id}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable("id") Long orderId, @RequestBody OrderDto updatedOrder){
        OrderDto orderDto = orderService.updateOrder(orderId, updatedOrder);
        return ResponseEntity.ok(orderDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/orderId/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") Long orderId){
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok("Order deleted.");
    }
}