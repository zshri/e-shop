package com.example.eshop.controller;

import com.example.eshop.model.Category;

import com.example.eshop.model.Product;
import com.example.eshop.model.exception.CategoryNotFoundException;
import com.example.eshop.model.exception.ProductNotFoundException;
import com.example.eshop.service.CartService;
import com.example.eshop.service.CategoryService;
import com.example.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;



@Controller
@RequestMapping("/catalog")
public class CatalogController {
    private final ProductService productService;
    private final CategoryService categoryService;
    @Autowired
    public CatalogController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }
    @GetMapping({"", "/{category}"})
    public String getCatalog(Model model,
                             @PathVariable(value = "category", required = false) String category,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             @RequestParam(defaultValue = "id") String sortBy,
                             @RequestParam(required = false) Double minPrice,
                             @RequestParam(required = false) Double maxPrice) {
        List<Category> allCategories = categoryService.getAllCategories();
        model.addAttribute("categories", allCategories);

        if (category != null) {
            Optional<Category> categoryOptional = allCategories.stream()
                    .filter(filterCategory -> filterCategory.getName().equals(category))
                    .findFirst();

            if (categoryOptional.isPresent()) {
                Category foundCategory = categoryOptional.get();
                Page<Product> pageProductsByCategory = productService.getProductPage(foundCategory, page, size, sortBy, minPrice, maxPrice);
                model.addAttribute("category", foundCategory.getName());
                model.addAttribute("products", pageProductsByCategory);
            } else {
                throw new CategoryNotFoundException("Category not found");
            }
        } else {
            Page<Product> products = productService.getProductPage(null, page, size, sortBy, minPrice, maxPrice);
            model.addAttribute("products", products);
        }
        return "catalog/catalogMain";
    }

    @GetMapping("/products/{id}")
    public String getProductDetails(@PathVariable Long id, Model model) {
        Optional<Product> productById = productService.findProductById(id);
        if (productById.isPresent()) {
            Category category = productById.get().getCategory();
            model.addAttribute("product", productById.get());
            model.addAttribute("category", category);
        } else {
            throw new ProductNotFoundException("Product not found");
        }
        return "catalog/productDetails";
    }
}