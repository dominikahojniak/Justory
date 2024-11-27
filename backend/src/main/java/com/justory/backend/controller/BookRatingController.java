package com.justory.backend.controller;

import com.justory.backend.api.external.BookRatingDTO;
import com.justory.backend.api.external.UsersDTO;
import com.justory.backend.service.BookRatingService;
import com.justory.backend.service.JwtService;
import com.justory.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book-rating")
public class BookRatingController {

    private final BookRatingService bookRatingService;
    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    public BookRatingController(BookRatingService bookRatingService, JwtService jwtService, UserService userService) {
        this.bookRatingService = bookRatingService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("addbook/{bookId}")
    public ResponseEntity<?> addRating(
            @PathVariable("bookId") Integer bookId,
            @RequestParam("rating") int rating,
            @CookieValue("token") String authorizationHeader) {
        try {
            Integer userId = getUserIdFromAuthorizationHeader(authorizationHeader);
            bookRatingService.addRating(userId, bookId, rating);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding rating");
        }
    }

    @GetMapping("/user-ratings")
    public ResponseEntity<?> getUserRatings(@CookieValue("token") String authorizationHeader) {
        try {
            Integer userId = getUserIdFromAuthorizationHeader(authorizationHeader);
            List<BookRatingDTO> userRatings = bookRatingService.getRatingsByUserId(userId);
            return ResponseEntity.ok(userRatings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }

    @DeleteMapping("removebook/{bookId}")
    public ResponseEntity<?> deleteRating(
            @PathVariable("bookId") Integer bookId,
            @CookieValue("token") String authorizationHeader) {
        try {
            Integer userId = getUserIdFromAuthorizationHeader(authorizationHeader);
            bookRatingService.deleteRating(userId, bookId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting rating");
        }
    }


    private Integer getUserIdFromAuthorizationHeader(String authorizationHeader) throws Exception {
        String userEmail = jwtService.extractEmail(authorizationHeader);
        UsersDTO user = userService.getUserByEmail(userEmail);
        if (user != null) {
            return user.getId();
        } else {
            throw new Exception("User not found for email: " + userEmail);
        }
    }
}
