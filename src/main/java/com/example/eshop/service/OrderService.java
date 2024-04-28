package com.example.eshop.service;


import com.example.eshop.model.*;
import com.example.eshop.model.exception.CartIsEmptyException;
import com.example.eshop.model.exception.OrderNotFoundException;
import com.example.eshop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;

    @Autowired
    public OrderService(CartService cartService, OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
    }

    public Order createOrder(User user, Order orderReq) {
        Cart cart = cartService.getCart(user);

        if (cart != null && !cart.getCartItems().isEmpty()) {

            List<OrderItem> orderItems = new ArrayList<>();

            Order order = Order.builder()
                    .shippingNotes(orderReq.getShippingNotes())
                    .phoneNumber(orderReq.getPhoneNumber())
                    .billingName(orderReq.getBillingName())
                    .paymentMethod(orderReq.getPaymentMethod())
                    .shippingAddress(orderReq.getShippingAddress())
                    .user(user)
                    .status(OrderStatus.PENDING)
                    .build();

            for (CartItem cartItem : cart.getCartItems()) {
                OrderItem orderItem = OrderItem.builder()
                        .product(cartItem.getProduct())
                        .quantity(cartItem.getQuantity())
                        .order(order)
                        .build();
                orderItems.add(orderItem);
            }

            order.setItems(orderItems);

            return orderRepository.save(order);
        } else {
            throw new CartIsEmptyException("Cart is empty. Cannot create an order.");
        }
    }
    public Order getOrder(User user, Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException( "Order not found"));
        if (!order.getUser().equals(user)) {
            throw new OrderNotFoundException( "Order not found");
        }
        return order;
    }
    public Page<Order> getOrderPage(User user, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Order> orderPage = orderRepository.findByUser(user, pageable);

        return orderPage;
    }
}
