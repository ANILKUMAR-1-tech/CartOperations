package com.eCommerce.CartOperations.service.cart;

import com.eCommerce.CartOperations.model.CartItem;
import com.eCommerce.CartOperations.model.Product;

public interface ICartItemService {
    void addCartItem(Long cartId, Long productId,int quantity);
    void removeItemFromCart(Long cartId,Long productId);
    void updateItemQuantity(Long cartId,Long productId,int quantity);

    CartItem getCartItem(Long cartId, Long productId);


}
