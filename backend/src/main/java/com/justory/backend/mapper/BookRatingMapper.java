package com.justory.backend.mapper;

import com.justory.backend.api.external.BookRatingDTO;
import com.justory.backend.api.internal.BookRating;
import com.justory.backend.api.internal.Books;
import com.justory.backend.api.internal.Users;
import com.justory.backend.repository.BooksRepository;
import com.justory.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookRatingMapper {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BooksMapper booksMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BooksRepository booksRepository;

    public BookRatingDTO toDTO(BookRating bookRating) {
        return new BookRatingDTO()
                .setId(bookRating.getId())
                .setUser(userMapper.toDTO(bookRating.getUser()))
                .setBook(booksMapper.toDTO(bookRating.getBook()))
                .setRating(bookRating.getRating());
    }

    public BookRating toEntity(BookRatingDTO bookRatingDTO) {
        Users user = userRepository.findById(bookRatingDTO.getUser().getId()).orElseThrow(() -> new RuntimeException("User not found"));
        Books book = booksRepository.findById(bookRatingDTO.getBook().getId()).orElseThrow(() -> new RuntimeException("Book not found"));
        return new BookRating()
                .setRating(bookRatingDTO.getRating())
                .setUser(user)
                .setBook(book);
    }
}
