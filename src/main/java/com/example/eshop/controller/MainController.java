package com.example.eshop.controller;


import com.example.eshop.model.Product;
import com.example.eshop.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MainController {
    @GetMapping
    String index(Model model) {
        return "main";
    }

    @GetMapping("/main")
    String mainPage(Model model, @AuthenticationPrincipal User user) {
        if (user != null) {
            model.addAttribute("username", user.getName());
        }
        return "main";
    }

    @GetMapping("/delivery")
    String deliveryPage(Model model, @AuthenticationPrincipal User user) {
        if (user != null) {
            model.addAttribute("username", user.getName());
        }
        return "info/delivery";
    }
    @GetMapping("/about")
    String aboutPage(Model model, @AuthenticationPrincipal User user) {
        if (user != null) {
            model.addAttribute("username", user.getName());
        }
        return "info/about";
    }



}
