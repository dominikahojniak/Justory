package com.justory.backend.repository;

import com.justory.backend.api.internal.BookRating;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRatingRepository extends JpaRepository<BookRating, Integer> {
    Optional<BookRating> findByUserIdAndBookId(Integer userId, Integer bookId);

    List<BookRating> findByUserId(Integer userId);
}
