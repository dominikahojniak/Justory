package com.justory.backend.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.justory.backend.api.external.BooksDTO;
import com.justory.backend.service.BooksService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BooksDTO addBook(
            @RequestPart("file") MultipartFile file,
            @RequestPart("bookDTO") String bookDTOJson) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        BooksDTO bookDTO;
        try {
            bookDTO = objectMapper.readValue(bookDTOJson, BooksDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error with JSON", e);
        }

        return booksService.addBookWithAvailabilities(bookDTO, file);

    }
}