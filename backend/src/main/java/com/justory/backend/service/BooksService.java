package com.justory.backend.service;

import com.justory.backend.api.external.BooksDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BooksService {
    List<BooksDTO> getAllBooks();
    BooksDTO getBookByTitle(String title);
    BooksDTO addBook(BooksDTO bookDTO);
    BooksDTO addBookWithAvailabilities(BooksDTO bookDTO, MultipartFile file);
}
