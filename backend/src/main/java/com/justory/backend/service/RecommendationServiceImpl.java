package com.justory.backend.service;


import com.justory.backend.api.external.AuthorsDTO;
import com.justory.backend.api.external.BookRatingDTO;
import com.justory.backend.api.external.BooksDTO;
import com.justory.backend.api.external.CategoriesDTO;
import com.justory.backend.api.internal.Authors;
import com.justory.backend.api.internal.Books;
import com.justory.backend.api.internal.Categories;
import com.justory.backend.mapper.BooksMapper;
import com.justory.backend.repository.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    @Autowired
    private UserToReadListService userToReadListService;

    @Autowired
    private BookRatingService bookRatingService;

    @Autowired
    private BooksRepository booksRepository;

    @Autowired
    private BooksMapper booksMapper;

    @Override
    public List<BooksDTO> getRecommendationsForUser(Integer userId) {
        List<BooksDTO> userToReadBooks = userToReadListService.getUserToReadBooks(userId);
        List<BookRatingDTO> userRatedBooks = bookRatingService.getRatingsByUserId(userId);
        if (userToReadBooks.isEmpty() && userRatedBooks.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, Double> categoryWeights = new HashMap<>();
        Map<String, Double> authorWeights = new HashMap<>();

        double totalWeight = 0.0;

        for (BookRatingDTO ratingDTO : userRatedBooks) {
            double ratingWeight = ratingDTO.getRating();
            BooksDTO book = ratingDTO.getBook();

            if (book.getCategories() != null) {
                for (CategoriesDTO category : book.getCategories()) {
                    categoryWeights.put(
                            category.getName(),
                            categoryWeights.getOrDefault(category.getName(), 0.0) + ratingWeight
                    );
                }
            }

            if (book.getAuthors() != null) {
                for (AuthorsDTO author : book.getAuthors()) {
                    String authorName = author.getFirstName() + " " + author.getLastName();
                    authorWeights.put(
                            authorName,
                            authorWeights.getOrDefault(authorName, 0.0) + ratingWeight
                    );
                }
            }

            totalWeight += ratingWeight;
        }
        double toReadWeight = 1.0; // stala, mala waga bo sa tylko w do przeczytania

        for (BooksDTO book : userToReadBooks) {
            if (book.getCategories() != null) {
                for (CategoriesDTO category : book.getCategories()) {
                    categoryWeights.put(
                            category.getName(),
                            categoryWeights.getOrDefault(category.getName(), 0.0) + toReadWeight
                    );
                    System.out.println(category.getName());
                }

            }

            if (book.getAuthors() != null) {
                for (AuthorsDTO author : book.getAuthors()) {
                    String authorName = author.getFirstName() + " " + author.getLastName();
                    authorWeights.put(
                            authorName,
                            authorWeights.getOrDefault(authorName, 0.0) + toReadWeight
                    );
                    System.out.println(authorName);
                }
            }
            totalWeight += toReadWeight;
        }

        double totalGenreCount = categoryWeights.values().stream().mapToDouble(Double::doubleValue).sum();
        double totalAuthorCount = authorWeights.values().stream().mapToDouble(Double::doubleValue).sum();
        System.out.println(totalAuthorCount);
        System.out.println(totalGenreCount);

        for (Map.Entry<String, Double> entry : categoryWeights.entrySet()) {
            categoryWeights.put(entry.getKey(), entry.getValue() / totalWeight);
        }

        for (Map.Entry<String, Double> entry : authorWeights.entrySet()) {
            authorWeights.put(entry.getKey(), entry.getValue() / totalWeight);
        }


        List<Books> potentialBooks = booksRepository.findBooksByCategoriesOrAuthors(
                new ArrayList<>(categoryWeights.keySet()),
                new ArrayList<>(authorWeights.keySet())
        );

        List<Integer> excludedBookIds = getUserExcludedBookIds(userId);

        Map<Books, Double> potentialBooksWithScore = new HashMap<>();

        for (Books book : potentialBooks) {
            if (excludedBookIds.contains(book.getId())) continue;

            double score = 0.0;

            if (book.getCategories() != null) {
                for (Categories category : book.getCategories()) {
                    Double weight = categoryWeights.get(category.getName());
                    if (weight != null) {
                        score += weight;
                    }
                }
            }

            if (book.getAuthors() != null) {
                for (Authors author : book.getAuthors()) {
                    String authorName = author.getFirstName() + " " + author.getLastName();
                    Double weight = authorWeights.get(authorName);
                    if (weight != null) {
                        score += weight;
                    }
                }
            }

            if (score > 0) {
                potentialBooksWithScore.put(book, score);
            }
        }

        List<Map.Entry<Books, Double>> sortedBooks = new ArrayList<>(potentialBooksWithScore.entrySet());
        sortedBooks.sort(Map.Entry.<Books, Double>comparingByValue().reversed());

        return sortedBooks.stream()
                .map(entry -> booksMapper.toDTO(entry.getKey()))
                .limit(10)
                .collect(Collectors.toList());
    }

    private List<Integer> getUserExcludedBookIds(Integer userId) {
        List<Integer> toReadBookIds = userToReadListService.getUserToReadBooks(userId)
                .stream().map(BooksDTO::getId).toList();
        List<Integer> ratedBookIds = bookRatingService.getRatingsByUserId(userId)
                .stream().map(rating -> rating.getBook().getId()).toList();


        List<Integer> excludedBookIds = new ArrayList<>();
        excludedBookIds.addAll(toReadBookIds);
        excludedBookIds.addAll(ratedBookIds);


        return excludedBookIds;
    }
}
