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
    public Cart getCart(User user) {
        Cart cartByUserId = cartRepository.findByUserId(user.getId());
        if (cartByUserId != null) {
            return cartByUserId;
        } else {
            Cart newCart = Cart.builder().user(user).build();
            return cartRepository.save(newCart);
        }
    }
    public String addProductToCart(Long productId, User user) {
        Cart cart = getCart(user);
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
            return product1.getName();
        }
        return null;
    }
    public void deleteCartItemToCart(Long idCartItem, User user) {
        Cart cart = cartRepository.findByUserId(user.getId());
        if (cart != null) {
            cart.getCartItems().removeIf(item -> item.getId().equals(idCartItem));
            cartRepository.save(cart);
        }
    }
    public void removeCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId());
        if (cart != null) {
            cart.getCartItems().clear();
            cartRepository.save(cart);
        }
    }
}
