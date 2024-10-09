package com.justory.backend.service;
import com.justory.backend.api.external.AuthenticationRequest;
import com.justory.backend.api.external.AuthenticationResponse;
import com.justory.backend.api.external.RegisterRequest;
import com.justory.backend.api.external.UsersDTO;
import org.springframework.stereotype.Service;


public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse login(AuthenticationRequest request);

    UsersDTO verify();
}