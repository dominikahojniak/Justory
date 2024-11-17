package com.justory.backend.service;

import com.justory.backend.api.external.BooksDTO;
import java.util.List;

public interface RecommendationService {
    List<BooksDTO> getRecommendationsForUser(Integer userId);
}
