package com.example.eshop.service;


import com.example.eshop.model.Cart;
import com.example.eshop.model.CartItem;
import com.example.eshop.model.Product;
import com.example.eshop.model.User;
import com.example.eshop.repository.CartRepository;
import com.example.eshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartService(ProductRepository productRepository, CartRepository cartRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public void addProductToCart(Long productId, User user) {
        Cart cart = cartRepository.findByUserId(user.getId());
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            Product product1 = product.get();
            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product1)
                    .quantity(1)
                    .build();
            cart.getCartItems().add(cartItem);
            cartRepository.save(cart);
        }
    }

    public Cart getCart(User user) {
        Cart cartByUserId = cartRepository.findByUserId(user.getId());
        if (cartByUserId != null) {
            return cartByUserId;
        } else {
            Cart newCart = Cart.builder().user(user).build();
            return cartRepository.save(newCart);
        }
    }

}
