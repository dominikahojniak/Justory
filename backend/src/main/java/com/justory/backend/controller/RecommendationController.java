package com.justory.backend.controller;

import com.justory.backend.api.external.BooksDTO;
import com.justory.backend.api.external.UsersDTO;
import com.justory.backend.service.JwtService;
import com.justory.backend.service.RecommendationService;
import com.justory.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private static final Logger log = LoggerFactory.getLogger(RecommendationController.class);
    @Autowired
    private RecommendationService recommendationService;
    private final JwtService jwtService;
    private final UserService userService;

    public RecommendationController(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<BooksDTO>> getRecommendations(@CookieValue("token") String authorizationHeader) {
        try {
            Integer userId = getUserIdFromAuthorizationHeader(authorizationHeader);
            List<BooksDTO> recommendations = recommendationService.getRecommendationsForUser(userId);
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
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