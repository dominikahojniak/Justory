package com.justory.backend.service;

import com.justory.backend.api.external.BookRatingDTO;

import java.util.List;

public interface BookRatingService {
    void addRating(Integer userId, Integer bookId, int rating) throws Exception;

    List<BookRatingDTO> getRatingsByUserId(Integer userId);

    void deleteRating(Integer userId, Integer bookId) throws Exception;
}