package com.example.eshop.service;

import com.example.eshop.model.Category;
import com.example.eshop.repository.CategoryRepository;
import com.example.eshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    @Autowired
    public CategoryService( CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

//    Кэш не настроен
//    @Cacheable(value = "categories", key = "allCategories")
//    public List<Category> getAllCategoriesCache() {
//        List<Category> categories = getAllCategories();
//        return categories;
//    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }



}
