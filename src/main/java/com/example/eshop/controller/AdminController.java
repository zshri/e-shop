package com.example.eshop.controller;


import com.example.eshop.model.Category;
import com.example.eshop.model.Product;
import com.example.eshop.model.User;
import com.example.eshop.service.CartService;
import com.example.eshop.service.CategoryService;
import com.example.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public AdminController(ProductService productService, CategoryService categoryService, CartService cartService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getAdminDashboard(Model model, Authentication authentication){
        return "admin/dashboard";
    }



    /* Admin features Catalog */

    @GetMapping("/products/new")
    public String showAddProductForm(Model model) {
        List<Category> allCategories = categoryService.getAllCategories();
        model.addAttribute("allCategories", allCategories);
        model.addAttribute("product", new Product());
        return "admin/productForm";
    }
    @PostMapping("/products")
    public String addProduct(@ModelAttribute("product") Product product, @AuthenticationPrincipal User user) {
        product.setCreateAt(Instant.now());
        product.setUser(user);
        if (product.getDescription().length() > 200) {
            product.setShortDescription(product.getDescription().substring(0,200) + "..");
        } else {
            product.setShortDescription(product.getDescription());
        }
        productService.saveProduct(product);
        return "redirect:/catalog/products/" + product.getId();
    }
    @GetMapping("/products/{id}/edit")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        model.addAttribute("allCategories", categoryService.getAllCategories());
        model.addAttribute("product", productService.findProductById(id).orElse(null));
        return "admin/productEditForm";
    }
    @PostMapping("/products/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute("product") Product product) {
        Optional<Product> existingProduct = productService.findProductById(id);
        if (existingProduct.isPresent()){
            Product productToUpdate = existingProduct.get();
            productToUpdate.setCategory(product.getCategory());
            productToUpdate.setName(product.getName());
            productToUpdate.setDescription(product.getDescription());
            if (product.getDescription().length() > 200) {
                productToUpdate.setShortDescription(product.getDescription().substring(0,200) + "..");
            } else {
                productToUpdate.setShortDescription(product.getDescription());
            }
            productService.saveProduct(productToUpdate);
        }
        return "redirect:/catalog/products/" + product.getId();
    }

    @DeleteMapping ("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/catalog";
    }

    @GetMapping("/category/new")
    public String showAddCategoryForm(Model model) {
        List<Category> allCategories = categoryService.getAllCategories();
        model.addAttribute("allCategories", allCategories);
        model.addAttribute("newCategory", new Category());
        return "admin/categoryForm";
    }
    @PostMapping("/category/new")
    public String addCategory(@ModelAttribute("newCategory")  Category category,
                              Model model,
                              BindingResult bindingResult) {
        try {
            categoryService.saveCategory(category);
            return "redirect:/admin/category/new";
        } catch (Exception e) {
            model.addAttribute("allCategories", categoryService.getAllCategories());
            model.addAttribute("error", "error");
            return "/admin/categoryForm";
        }
    }

}
