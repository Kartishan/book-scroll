package com.kartishan.bookscroll.controller;

import com.kartishan.bookscroll.model.Category;
import com.kartishan.bookscroll.model.dto.BookWithCategoriesDTO;
import com.kartishan.bookscroll.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping("/all")
    public ResponseEntity<?> getAllCategories(){
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok().body(categories);
    }
    @GetMapping("/categories")
    public List<String> getAllCategoryNames() {
        return categoryService.getAllCategoryNames();
    }
}
