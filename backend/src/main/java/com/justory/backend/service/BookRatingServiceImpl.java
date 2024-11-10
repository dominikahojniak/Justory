package com.justory.backend.service;

import com.justory.backend.api.external.BookRatingDTO;
import com.justory.backend.api.internal.BookRating;
import com.justory.backend.api.internal.Books;
import com.justory.backend.api.internal.Users;
import com.justory.backend.mapper.BookRatingMapper;
import com.justory.backend.repository.BookRatingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class BookRatingServiceImpl implements BookRatingService {

    private final BookRatingRepository bookRatingRepository;
    private final BookRatingMapper bookRatingMapper;

    @Autowired
    public BookRatingServiceImpl(BookRatingRepository bookRatingRepository, BookRatingMapper bookRatingMapper) {
        this.bookRatingRepository = bookRatingRepository;
        this.bookRatingMapper = bookRatingMapper;
    }

    @Override
    public void addRating(Integer userId, Integer bookId, int rating) throws Exception {
        Optional<BookRating> existingRating = bookRatingRepository.findByUserIdAndBookId(userId, bookId);

        if (existingRating.isPresent()) {
            throw new Exception("Book is already rated by user");
        } else {
            BookRating newRating = new BookRating()
                    .setUser(new Users().setId(userId))
                    .setBook(new Books().setId(bookId))
                    .setRating(rating);
            bookRatingRepository.save(newRating);
        }
    }

    @Override
    public List<BookRatingDTO> getRatingsByUserId(Integer userId) {
        List<BookRating> ratings = bookRatingRepository.findByUserId(userId);
        return ratings.stream()
                .map(bookRatingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteRating(Integer userId, Integer bookId) throws Exception {
        Optional<BookRating> entry = bookRatingRepository.findByUserIdAndBookId(userId, bookId);
        if (entry.isPresent()) {
            bookRatingRepository.delete(entry.get());
            System.out.println("Successfully removed book with ID " + bookId + " from user's Rated list");
        } else {
            System.out.println("Book with ID " + bookId + " not found in user's Rated list");
        }
    }
}