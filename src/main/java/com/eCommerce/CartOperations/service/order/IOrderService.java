package com.eCommerce.CartOperations.service.order;

import com.eCommerce.CartOperations.dto.OrderDto;
import com.eCommerce.CartOperations.model.Order;

import java.util.List;

public interface IOrderService {

    Order placeOrder(Long userId);
    OrderDto getorder(Long orderId);

    List<OrderDto> getUserOrders(Long userId);

    OrderDto convertedToDto(Order order);
}
