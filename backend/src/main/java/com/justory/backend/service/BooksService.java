package com.justory.backend.service;

import com.justory.backend.api.external.BooksDTO;

import java.util.List;

public interface BooksService {
    List<BooksDTO> getAllBooks();
    BooksDTO getBookByTitle(String title);
}
