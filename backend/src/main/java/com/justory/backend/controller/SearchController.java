package com.justory.backend.controller;

import com.justory.backend.api.external.BooksDTO;
import com.justory.backend.service.BooksService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    private final BooksService booksService;

    public SearchController(BooksService booksService) {
        this.booksService = booksService;
    }

    @GetMapping
    public ResponseEntity<List<BooksDTO>> searchBooks(@RequestParam("query") String query) {
        List<BooksDTO> searchResults = booksService.searchBooks(query);
        return ResponseEntity.ok().body(searchResults);
    }
}