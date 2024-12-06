package com.justory.backend;

import com.justory.backend.api.external.UsersDTO;
import com.justory.backend.api.internal.Users;
import com.justory.backend.api.internal.UsersFeatures;
import com.justory.backend.mapper.UserMapper;
import com.justory.backend.repository.UserRepository;
import com.justory.backend.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testGetUserByID_ShouldReturnUser_WhenUserExists() {
        Integer userId = 1;
        Users user = new Users()
                .setId(userId)
                .setEmail("test@example.com")
                .setName("Test User")
                .setRole("USER");
        UsersDTO usersDTO = new UsersDTO()
                .setId(userId)
                .setEmail("test@example.com")
                .setName("Test User")
                .setRole("USER");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(usersDTO);

        UsersDTO result = userService.getUserById(userId);

        assertNotNull(result, "User not found");
        assertEquals(userId, result.getId(), "User ID not found");
        assertEquals("test@example.com", result.getEmail(), "Email not match");
        verify(userRepository).findById(userId);
        verify(userMapper).toDTO(user);
    }

    @Test
    void testGetUserByID_ShouldReturnUser_WhenUserDoesNotExist() {
        Integer userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        UsersDTO result = userService.getUserById(userId);
        assertNull(result, "User not found");
        verify(userRepository).findById(userId);
        verifyNoInteractions(userMapper);
    }

    @Test
    void testGetUserByEmail_ShouldReturnUser_WhenUserExists() {
        String email = "test@example.com";
        Users user = new Users()
                .setId(1)
                .setEmail(email)
                .setName("Test User")
                .setRole("USER");
        UsersDTO usersDTO = new UsersDTO()
                .setId(1)
                .setEmail(email)
                .setName("Test User")
                .setRole("USER");

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(usersDTO);

        UsersDTO result = userService.getUserByEmail(email);

        assertNotNull(result, "User not found");
        assertEquals(email, result.getEmail(), "Email not match");
        verify(userRepository).findUserByEmail(email);
        verify(userMapper).toDTO(user);
    }

    @Test
    void testGetUserByEmail_ShouldReturnNull_WhenUserDoesNotExist() {
        String email = "test@example.com";
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        UsersDTO result = userService.getUserByEmail(email);

        assertNull(result, "User should be null when not found");
        verify(userRepository).findUserByEmail(email);
        verifyNoInteractions(userMapper);
    }

    @Test
    void testGetUserFeaturesByEmail_ShouldReturnFeatures_WhenUserExistsWithFeatures() {
        String email = "test@example.com";
        UsersFeatures userFeatures = new UsersFeatures()
                .setId(1)
                .setPhone(123456789L);
        Users user = new Users()
                .setEmail(email)
                .setUserFeaturesID(userFeatures);

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));

        UsersFeatures result = userService.getUserFeaturesByEmail(email);

        assertNotNull(result, "User features not found");
        assertEquals(1, result.getId(), "User Features ID not match");
        assertEquals(123456789L, result.getPhone(), "Phone number not match");
        verify(userRepository).findUserByEmail(email);
    }

    @Test
    void testGetUserFeaturesByEmail_ShouldReturnNull_WhenUserExistsWithoutFeatures() {
        String email = "test@example.com";
        Users user = new Users().setEmail(email);

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));

        UsersFeatures result = userService.getUserFeaturesByEmail(email);

        assertNull(result, "User features should be null");
        verify(userRepository).findUserByEmail(email);
    }

    @Test
    void testGetUserFeaturesByEmail_ShouldReturnNull_WhenUserDoesNotExist() {
        String email = "test@example.com";
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        UsersFeatures result = userService.getUserFeaturesByEmail(email);

        assertNull(result, "User features should be null when user not found");
        verify(userRepository).findUserByEmail(email);
    }

}
