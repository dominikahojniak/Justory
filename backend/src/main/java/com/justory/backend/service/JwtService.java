package com.justory.backend.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;
public interface JwtService {
    public String extractEmail(String jwtToken);

    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver);

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    public String generateToken(UserDetails userDetails);

    public boolean isTokenValid(String jwtToken, UserDetails userDetails);
}