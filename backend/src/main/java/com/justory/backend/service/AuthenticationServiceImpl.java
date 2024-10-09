package com.justory.backend.service;

import com.justory.backend.api.external.UsersDTO;
import com.justory.backend.api.internal.*;
import com.justory.backend.api.external.AuthenticationResponse;
import com.justory.backend.api.external.AuthenticationRequest;
import com.justory.backend.api.external.RegisterRequest;
import com.justory.backend.mapper.UserMapper;
import com.justory.backend.repository.UserRepository;
import com.justory.backend.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User already exists!");
        }
        UsersFeatures userFeatures = new UsersFeatures();
        userFeatures.setPhone(request.getPhone());
        Users user = new Users();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        user.setUserFeaturesID(userFeatures);
        userFeatures.setUser(user);
        userRepository.save(user);

        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findUserByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }

    @Override
    public UsersDTO verify() {
        var user = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userMapper.toDTO(user);
    }
}