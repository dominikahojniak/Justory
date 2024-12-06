package com.justory.backend;

import com.justory.backend.api.external.AuthenticationRequest;
import com.justory.backend.api.external.AuthenticationResponse;
import com.justory.backend.api.external.RegisterRequest;
import com.justory.backend.api.internal.Users;
import com.justory.backend.api.external.UsersDTO;
import com.justory.backend.mapper.UserMapper;
import com.justory.backend.repository.UserRepository;
import com.justory.backend.service.AuthenticationServiceImpl;
import com.justory.backend.service.JwtService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void register_ShouldRegisterUser_WhenUserDoesNotExist() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setName("Test User");
        request.setPassword("password");
        request.setPhone(123456789L);

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(Users.class))).thenReturn("jwtToken");

        AuthenticationResponse response = authenticationService.register(request);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());

        ArgumentCaptor<Users> userCaptor = ArgumentCaptor.forClass(Users.class);
        verify(userRepository).save(userCaptor.capture());
        Users savedUser = userCaptor.getValue();

        assertEquals(request.getEmail(), savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals("Test User", savedUser.getName());
        assertEquals("USER", savedUser.getRole());
        assertNotNull(savedUser.getUserFeaturesID());
        assertEquals(request.getPhone(), savedUser.getUserFeaturesID().getPhone());
        assertSame(savedUser, savedUser.getUserFeaturesID().getUser());
    }

    @Test
    void register_ShouldThrowException_WhenUserAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authenticationService.register(request);
        });

        assertEquals("User already exists!", exception.getMessage());
        verify(userRepository, never()).save(any(Users.class));
    }

    @Test
    void login_ShouldAuthenticateAndReturnToken_WhenCredentialsAreCorrect() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        Users user = new Users();
        user.setEmail(request.getEmail());

        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwtToken");

        AuthenticationResponse response = authenticationService.login(request);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findUserByEmail(request.getEmail());
        verify(jwtService).generateToken(user);
    }

    @Test
    void login_ShouldThrowException_WhenAuthenticationFails() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("test@example.com");
        request.setPassword("wrong-password");

        doThrow(new RuntimeException("Bad credentials"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authenticationService.login(request);
        });

        assertEquals("Bad credentials", exception.getMessage());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, never()).findUserByEmail(anyString());
        verify(jwtService, never()).generateToken(any(Users.class));
    }

    @Test
    void login_ShouldThrowException_WhenUserNotFound() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authenticationService.login(request);
        });

        assertEquals("User not found", exception.getMessage());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findUserByEmail(request.getEmail());
        verify(jwtService, never()).generateToken(any(Users.class));
    }

    @Test
    void verify_ShouldReturnUsersDTO_WhenUserIsAuthenticated() {
        Users user = new Users();
        user.setEmail("test@example.com");

        UsersDTO usersDTO = new UsersDTO();
        usersDTO.setEmail("test@example.com");

        when(userMapper.toDTO(user)).thenReturn(usersDTO);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        UsersDTO result = authenticationService.verify();

        assertNotNull(result);
        assertEquals(usersDTO, result);
        verify(userMapper).toDTO(user);
    }
}

