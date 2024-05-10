package com.example.eshop.controller;


import com.example.eshop.model.Category;
import com.example.eshop.model.Product;
import com.example.eshop.model.User;
import com.example.eshop.service.CartService;
import com.example.eshop.service.CategoryService;
import com.example.eshop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public AdminController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getAdminDashboard(){
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
    public String addProduct(@Valid @ModelAttribute("product") Product product,
                             BindingResult bindingResult,
                             RedirectAttributes attributes,
                             @AuthenticationPrincipal User user) {
        if (bindingResult.hasErrors()) {
            attributes.addFlashAttribute("error", "validation error");
            return "redirect:/admin/products/new";
        }
        try {
            product.setCreateAt(Instant.now());
            product.setUser(user);
            product.setDeleted(false);
            if (product.getDescription().length() > 200) {
                product.setShortDescription(product.getDescription().substring(0,200) + "..");
            } else {
                product.setShortDescription(product.getDescription());
            }
            productService.saveProduct(product);
            return "redirect:/catalog/products/" + product.getId();
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "unknown error");
            return "redirect:/admin/products/new";
        }

    }
    @GetMapping("/products/{productId}/edit")
    public String showEditProductForm(@PathVariable(value = "productId") Long id, Model model) {
        model.addAttribute("allCategories", categoryService.getAllCategories());
        model.addAttribute("product", productService.findProductById(id).orElse(null));
        return "admin/productEditForm";
    }
    @PostMapping("/products/{productId}")
    public String updateProduct(@PathVariable(value = "productId") Long id,
                                @Valid @ModelAttribute("product") Product product,
                                BindingResult bindingResult,
                                RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            attributes.addFlashAttribute("error", "validation error");
            return "redirect:/admin/products/" + id + "/edit";
        }
        try {
            Optional<Product> existingProduct = productService.findProductById(id);
            if (existingProduct.isPresent()){
                Product productToUpdate = existingProduct.get();
                productToUpdate.setCategory(product.getCategory());
                productToUpdate.setName(product.getName());
                productToUpdate.setDescription(product.getDescription());
                productToUpdate.setPrice(product.getPrice());
                if (product.getDescription().length() > 200) {
                    productToUpdate.setShortDescription(product.getDescription().substring(0,200) + "..");
                } else {
                    productToUpdate.setShortDescription(product.getDescription());
                }
                productService.saveProduct(productToUpdate);
            }
            return "redirect:/catalog/products/" + product.getId();
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "unknown error");
            return "redirect:/admin/products/" + id + "/edit";
        }

    }

    @PostMapping ("/products/{id}/delete")
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
    public String addCategory(@Valid @ModelAttribute("newCategory")  Category category,
                              BindingResult bindingResult,
                              RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            attributes.addFlashAttribute("error", "validation error");
            return "redirect:/admin/category/new";
        }
        try {
            categoryService.saveCategory(category);
            attributes.addFlashAttribute("message", "Category add - " + category.getName());
            return "redirect:/admin/category/new";
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "unknown error");
            return "/admin/categoryForm";
        }
    }

}
