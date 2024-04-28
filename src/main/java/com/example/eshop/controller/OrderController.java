package com.example.eshop.controller;

import com.example.eshop.model.*;
import com.example.eshop.model.exception.CartIsEmptyException;
import com.example.eshop.service.CartService;
import com.example.eshop.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    @Autowired
    public OrderController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @GetMapping("/checkout")
    public String getCheckoutForm(Model model,
                                  @AuthenticationPrincipal User user){
        model.addAttribute("orderReq", new Order());
        Cart cart = cartService.getCart(user);
        model.addAttribute("cart", cart );
        return "order/checkout";
    }

    @PostMapping("/order")
    public String addOrder(@Valid @ModelAttribute("orderReq") Order orderReq,
                           BindingResult bindingResult,
                           @AuthenticationPrincipal User user,
                           RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            attributes.addFlashAttribute("message", "validation error");
            return "redirect:/checkout";
        }
        try {
            Order order = orderService.createOrder(user, orderReq);
            return "redirect:/order/" + order.getId();
        } catch (CartIsEmptyException e) {
            attributes.addFlashAttribute("message", "Cart is empty");
            return "redirect:/checkout";
        }
    }

    @GetMapping("/order/{orderId}")
    public String getOrder(@PathVariable("orderId") Long orderId,
                           @AuthenticationPrincipal User user,
                           Model model) {
        Order order = orderService.getOrder(user, orderId);
        model.addAttribute("order",order);
        return "order/order";
    }

    @GetMapping("/orders")
    public String getOrderList(Model model,
                               @AuthenticationPrincipal User user,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "id") String sortBy
                               ) {
        Page<Order> orderPage = orderService.getOrderPage(user, page, size, sortBy);
        model.addAttribute("ordersPage",orderPage);

        return "order/orders";
    }
}
