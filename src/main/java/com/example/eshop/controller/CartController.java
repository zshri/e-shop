package com.example.eshop.controller;


import com.example.eshop.model.Cart;
import com.example.eshop.model.User;
import com.example.eshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    @GetMapping
    public String getCart(Model model,
                          RedirectAttributes redirectAttributes,
                          @AuthenticationPrincipal User user){
        if (user != null)  {
            String message = (String) redirectAttributes.getFlashAttributes().get("message");
            if (message != null) {
                model.addAttribute("message", message);
            }
            Cart cart = cartService.getCart(user);
            model.addAttribute("cart", cart );
            return "cart";
        } else {
            return "redirect:/login";
        }
    }
    @PostMapping("/{idProduct}/add")
    public RedirectView addToCart(@PathVariable("idProduct") Long idProduct,
                                  RedirectAttributes attributes,
                                  @AuthenticationPrincipal User user) {
        if (user != null)  {
            cartService.addProductToCart(idProduct, user);
            attributes.addFlashAttribute("message", "Product with id " + idProduct + " added to cart.");
            return new RedirectView("/cart");
        } else {
            return new RedirectView("/login");
        }
    }
    @PostMapping("/{idProduct}/delete")
    public RedirectView delToCart(@PathVariable("idProduct") Long idProduct,
                                  RedirectAttributes attributes,
                                  @AuthenticationPrincipal User user) {
        if (user != null)  {
            cartService.deleteProductToCart(idProduct, user);
            attributes.addFlashAttribute("message", "Product with id " + idProduct + " delete to cart.");
            return new RedirectView("/cart");
        } else {
            return new RedirectView("/login");
        }
    }
    @PostMapping("/remove")
    public RedirectView removeCart(RedirectAttributes attributes,
                                  @AuthenticationPrincipal User user) {
        if (user != null)  {
            cartService.removeCart(user);
            attributes.addFlashAttribute("message", "Cart removed.");
            return new RedirectView("/cart");
        } else {
            return new RedirectView("/login");
        }
    }
}
