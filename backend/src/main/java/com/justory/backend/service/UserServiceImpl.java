package com.justory.backend.service;

import com.justory.backend.api.external.UsersDTO;
import com.justory.backend.api.internal.UsersFeatures;
import com.justory.backend.api.internal.Users;
import com.justory.backend.mapper.UserMapper;
import com.justory.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UsersDTO getUserById(Integer id) {
        Optional<Users> userOptional = userRepository.findById(id);
        return userOptional.map(userMapper::toDTO).orElse(null);
    }
    @Override
    public UsersDTO getUserByEmail(String email) {
        Optional<Users> userOptional = userRepository.findUserByEmail(email);
        return userOptional.map(userMapper::toDTO).orElse(null);
    }
    public UsersFeatures getUserFeaturesByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .map(Users::getUserFeaturesID)
                .orElse(null);
    }
}