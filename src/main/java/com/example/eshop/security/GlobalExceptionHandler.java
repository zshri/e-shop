package com.example.eshop.security;

import com.example.eshop.model.exception.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler({CartIsEmptyException.class,
//            CategoryNotFoundException.class,
//            OrderNotFoundException.class,
//            ProductNotFoundException.class,
//            UserAlreadyExistsException.class})
//    public String handleException(Model model, Exception ex) {
//        model.addAttribute("errorMessage", ex.getMessage());
//        return "error";
//    }
    @ExceptionHandler(Exception.class)
    public String handleException(Model model, Exception ex) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

}