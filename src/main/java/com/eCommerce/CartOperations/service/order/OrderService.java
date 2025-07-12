package com.eCommerce.CartOperations.service.order;

import com.eCommerce.CartOperations.dto.OrderDto;
import com.eCommerce.CartOperations.enums.OrderStatus;
import com.eCommerce.CartOperations.exceptions.ResourceNotFoundException;
import com.eCommerce.CartOperations.model.Cart;
import com.eCommerce.CartOperations.model.Order;
import com.eCommerce.CartOperations.model.OrderItem;
import com.eCommerce.CartOperations.model.Product;
import com.eCommerce.CartOperations.repository.OrderRepository;
import com.eCommerce.CartOperations.repository.ProductRepository;
import com.eCommerce.CartOperations.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;

    @Override
    public Order placeOrder(Long userId) {
        Cart cart=cartService.getCartByUserId(userId);
        System.out.println("Cart found for user: " + userId);
        System.out.println("Cart item count: " + cart.getCartItems().size());
        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Your cart is empty. Please add items to place an order.");
        }
        Order order = createOrder(cart);
        List<OrderItem> orderItemsList=createOrderItems(order,cart);
        System.out.println("Order items created: " + orderItemsList.size());
        order.setOrderItems(new HashSet<>(orderItemsList));
        order.setTotalAmount(calculateTotalAmount(orderItemsList));
       Order savedOrder = orderRepository.save(order);
        System.out.println("Order saved with ID: " + savedOrder.getId());
       cartService.clearCart(cart.getId());
        return savedOrder;
    }

    private  Order createOrder(Cart cart){
      Order order= new Order();
      order.setUser(cart.getUser());
      order.setOrderStatus(OrderStatus.PENDING);
      order.setOrderDate(LocalDate.now());
      return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart){
        return cart.getCartItems().stream().map(cartItem->{
            Product product=cartItem.getProduct();
            productRepository.save(product);
            return  new OrderItem(
                    order,
                    product,
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice());
        }).toList();
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItems){
        return orderItems.stream()
                .map(orderItem -> orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }





    @Override
    public OrderDto getorder(Long orderId) {
        return orderRepository.findById(orderId).map(this::convertedToDto).orElseThrow(()->new ResourceNotFoundException("order not found"));
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        List<Order> orders=orderRepository.findByUserId(userId);
        return orders.stream().map(this::convertedToDto).toList();

    }

    @Override
    public OrderDto convertedToDto(Order order){
        return modelMapper.map(order,OrderDto.class);
    }

}
