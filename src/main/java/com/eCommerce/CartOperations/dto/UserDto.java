package com.eCommerce.CartOperations.dto;

import com.eCommerce.CartOperations.model.Cart;
import com.eCommerce.CartOperations.model.Order;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<OrderDto> orders;
    private CartDto cart;
}
