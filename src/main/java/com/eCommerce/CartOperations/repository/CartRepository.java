package com.eCommerce.CartOperations.repository;

import com.eCommerce.CartOperations.model.Cart;
import com.eCommerce.CartOperations.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    Cart findByuserId(Long userId);
}
