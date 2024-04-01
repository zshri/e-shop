package com.example.eshop.controller;

import com.example.eshop.model.User;
import com.example.eshop.model.exception.UserAlreadyExistsException;
import com.example.eshop.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLoginForm(Model model) {
        return "user/login";
    }

    @GetMapping("/registration")
    public String getRegistrationForm(Model model) {
        model.addAttribute("newUser", new User());
        return "user/registration";
    }

    @PostMapping("/registration")
    public String registrationUser(@ModelAttribute("newUser") @Valid User newUser,
                                   BindingResult bindingResult,
                                   Model model,
                                   HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "user/registration";
        }
        try {
            User saveUser = userService.registrationUser(newUser);
            Authentication authentication = new UsernamePasswordAuthenticationToken(saveUser, null, ((UserDetails) saveUser).getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
            return "redirect:/";
        } catch (UserAlreadyExistsException e) {
            model.addAttribute("errorUserExist", e.getMessage());
            return "user/registration";
        } catch (Exception e) {
            model.addAttribute("error", "");
            return "user/registration";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        SecurityContextHolder.clearContext();
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('ROLE_USER') || hasAuthority('ROLE_ADMIN')")
    public String getProfile(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("authorities", user.getAuthorities());
        return "user/profile";
    }

}