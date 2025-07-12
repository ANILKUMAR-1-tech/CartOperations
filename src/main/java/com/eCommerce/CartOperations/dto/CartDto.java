package com.eCommerce.CartOperations.dto;

import com.eCommerce.CartOperations.model.CartItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
public class CartDto {
    private Long id;
    private Set<CartItemDto> cartItems;
    private BigDecimal totalAmount;

}
