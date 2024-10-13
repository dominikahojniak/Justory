package com.justory.backend.service;


import com.justory.backend.api.external.BooksDTO;
import com.justory.backend.api.internal.Books;
import com.justory.backend.mapper.BooksMapper;
import com.justory.backend.repository.BooksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BooksServiceImpl implements BooksService {

    private final BooksRepository booksRepository;
    private final BooksMapper booksMapper;

    @Override
    public List<BooksDTO> getAllBooks() {
        List<Books> booksList = booksRepository.findAll();
        return booksList.stream()
                .map(booksMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BooksDTO getBookByTitle(String title) {
        List<Books> booksList = booksRepository.findByTitle(title);
        return booksList.isEmpty() ? null : booksMapper.toDTO(booksList.get(0));
    }
}
