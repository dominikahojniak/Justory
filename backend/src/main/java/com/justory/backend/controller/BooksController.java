package com.justory.backend.controller;


import com.justory.backend.api.external.BooksDTO;
import com.justory.backend.service.BooksService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BooksController {

    private final BooksService booksService;

    @GetMapping
    public List<BooksDTO> getAllBooks() {
        return booksService.getAllBooks();
    }

    @GetMapping("/title/{title}")
    public BooksDTO getBookByTitle(@PathVariable String title) {
        return booksService.getBookByTitle(title);
    }
}