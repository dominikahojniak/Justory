package com.justory.backend.controller;

import com.justory.backend.api.external.BooksDTO;
import com.justory.backend.api.external.CategoriesDTO;
import com.justory.backend.api.internal.Categories;
import com.justory.backend.service.BooksService;
import com.justory.backend.service.CategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoriesController {

    private final CategoriesService categoriesService;
    private final BooksService booksService;

    @GetMapping
    public List<CategoriesDTO> getAllCategories() {
        return categoriesService.getAllCategories();
    }
    @GetMapping("/{categoryName}/books")
    public ResponseEntity<Map<String, Object>> getBooksByCategory(@PathVariable String categoryName) {
        Categories category = categoriesService.findByName(categoryName);
        List<BooksDTO> books = booksService.findBooksByCategory(categoryName);

        Map<String, Object> response = new HashMap<>();
        response.put("books", books);
        response.put("categoryName", category.getName());

        return ResponseEntity.ok(response);
    }
}