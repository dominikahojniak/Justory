package com.justory.backend.controller;

import com.justory.backend.api.external.UsersDTO;
import com.justory.backend.api.external.UsersFeaturesDTO;
import com.justory.backend.api.internal.Users;
import com.justory.backend.api.internal.UsersFeatures;
import com.justory.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private final UserService userService;
    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsersDTO> getUser(@PathVariable Integer id) {
        UsersDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok().body(userDTO);
    }
    @GetMapping("/profile")
    public ResponseEntity<UsersDTO> getUserProfile(Authentication authentication) {
        Users currentUser = (Users) authentication.getPrincipal();
        String userEmail = currentUser.getEmail();
        UsersDTO userDTO = userService.getUserByEmail(userEmail);
        if (userDTO != null) {
            UsersFeatures userFeatures = userService.getUserFeaturesByEmail(userEmail);
            if (userFeatures != null) {
                UsersFeaturesDTO userFeaturesDTO = new UsersFeaturesDTO().setPhone(userFeatures.getPhone());
                userDTO.setUserFeaturesID(userFeaturesDTO);
            }
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}