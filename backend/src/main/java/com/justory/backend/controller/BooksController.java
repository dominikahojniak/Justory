package com.justory.backend.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.justory.backend.api.external.BooksDTO;
import com.justory.backend.service.BooksService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

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

    @PutMapping(value = "/edit/{bookId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BooksDTO updateBook(
            @PathVariable Integer bookId,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart("bookDTO") String bookDTOJson) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        BooksDTO bookDTO;
        try {
            bookDTO = objectMapper.readValue(bookDTOJson, BooksDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON", e);
        }

        return booksService.updateBook(bookId, bookDTO, file);
    }

    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable Integer bookId) {
        try {
            booksService.deleteBookById(bookId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting book" + e.getMessage());
        }
    }
    @GetMapping("/premieres")
    public List<BooksDTO> getPremieresForCurrentMonth() {
        LocalDate currentDate = LocalDate.now();
        YearMonth currentYearMonth = YearMonth.from(currentDate);
        List<BooksDTO> allBooks = booksService.getAllBooks();
        List<BooksDTO> premieresForCurrentMonth = allBooks.stream()
                .filter(book -> {
                    YearMonth premiereYearMonth = YearMonth.from(book.getDate());
                    return premiereYearMonth.equals(currentYearMonth);
                })
                .collect(Collectors.toList());
        return premieresForCurrentMonth;
    }
}