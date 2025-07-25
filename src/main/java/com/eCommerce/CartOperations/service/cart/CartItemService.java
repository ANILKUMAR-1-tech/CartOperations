package com.eCommerce.CartOperations.service.cart;

import com.eCommerce.CartOperations.exceptions.ResourceNotFoundException;
import com.eCommerce.CartOperations.model.Cart;
import com.eCommerce.CartOperations.model.CartItem;
import com.eCommerce.CartOperations.model.Product;
import com.eCommerce.CartOperations.repository.CartItemRepository;
import com.eCommerce.CartOperations.repository.CartRepository;
import com.eCommerce.CartOperations.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
    private final CartItemRepository cartItemRepository;
    private final IProductService productService;
    private final ICartService cartService;
    private final CartRepository cartRepository;@Override
    public void addCartItem(Long cartId, Long productId, int quantity) {
        Cart cart=cartService.getCart(cartId);
        Product product=productService.getProductById(productId);
        CartItem cartItem=cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElse(new CartItem());
        if(cartItem.getId() == null){
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setCart(cart);
            cartItem.setUnitPrice(product.getPrice());
        }
        else{
            cartItem.setQuantity(cartItem.getQuantity()+quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart=cartService.getCart(cartId);
         CartItem itemToRemove=getCartItem(cartId,productId);
         cart.removeItem(itemToRemove);
         cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
         Cart cart=cartService.getCart(cartId);
         cart.getCartItems()
                 .stream()
                 .filter(item -> item.getProduct().getId().equals(productId))
                 .findFirst()
                 .ifPresent(item -> {
                     item.setQuantity(quantity);
                     item.setUnitPrice(item.getProduct().getPrice());
                     item.setTotalPrice();
                 });
        BigDecimal totalAmount=cart.getCartItems().stream()
                .map(CartItem ::getTotalPrice).reduce(BigDecimal.ZERO,BigDecimal :: add);
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);

    }
    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
         return cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

    }


}
