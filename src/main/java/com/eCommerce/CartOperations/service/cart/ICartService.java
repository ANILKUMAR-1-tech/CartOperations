package com.eCommerce.CartOperations.service.cart;

import com.eCommerce.CartOperations.model.Cart;
import com.eCommerce.CartOperations.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);


    Cart initilizeNewCart(User user);

    Cart getCartByUserId(Long userId);

}
