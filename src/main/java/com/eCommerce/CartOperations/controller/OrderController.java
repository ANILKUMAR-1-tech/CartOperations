package com.eCommerce.CartOperations.controller;


import com.eCommerce.CartOperations.dto.OrderDto;
import com.eCommerce.CartOperations.exceptions.ResourceNotFoundException;
import com.eCommerce.CartOperations.model.Order;
import com.eCommerce.CartOperations.response.ApiResponse;
import com.eCommerce.CartOperations.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final IOrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<ApiResponse>  createOrder(@RequestParam Long userId){
        try {
            Order order= orderService.placeOrder(userId);
            OrderDto orderDto=orderService.convertedToDto(order);
            return  ResponseEntity.ok(new ApiResponse("success",orderDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error",e.getMessage()));
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse> getOrdersById(@PathVariable Long orderId){
        try {
            OrderDto order=orderService.getorder(orderId);
            return ResponseEntity.ok(new ApiResponse("order Item Found",order));
        } catch (ResourceNotFoundException e) {
           return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("Oops!",e.getMessage()));
        }
    }
    @GetMapping("/order/{userId}/userOrders")
    public ResponseEntity<ApiResponse> getUserOrderById(@PathVariable Long userId){
        try {
            List<OrderDto> order=orderService.getUserOrders(userId);
            return ResponseEntity.ok(new ApiResponse("order Item Found",order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("Oops!",e.getMessage()));
        }
    }
}
