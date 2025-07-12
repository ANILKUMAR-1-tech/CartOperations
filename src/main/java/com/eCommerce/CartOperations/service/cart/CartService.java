package com.eCommerce.CartOperations.service.cart;


import com.eCommerce.CartOperations.exceptions.ResourceNotFoundException;
import com.eCommerce.CartOperations.model.Cart;
import com.eCommerce.CartOperations.model.User;
import com.eCommerce.CartOperations.repository.CartItemRepository;
import com.eCommerce.CartOperations.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService{
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AtomicLong cartIdGenerator=new AtomicLong();

    @Override
    public Cart getCart(Long id) {
        Cart cart=cartRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Cart not found"));
        BigDecimal totalAmount=cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart=getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getCartItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.save(cart);

    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart=getCart(id);
        return cart.getTotalAmount();
    }

    @Override
    public Cart initilizeNewCart(User user){
       return Optional.ofNullable(getCartByUserId(user.getId())).orElseGet(()-> {
           Cart cart =new Cart();
           cart.setUser(user);
           return cartRepository.save(cart);

       });
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return  cartRepository.findByuserId(userId);
    }



}
