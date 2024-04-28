package com.example.eshop.controller;


import com.example.eshop.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping
    String index() {
        return "redirect:/catalog";
    }


    @GetMapping("/about")
    String aboutPage(Model model, @AuthenticationPrincipal User user) {
        if (user != null) {
            model.addAttribute("username", user.getName());
        }
        return "about";
    }



}
