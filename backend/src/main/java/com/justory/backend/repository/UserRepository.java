package com.justory.backend.repository;


import com.justory.backend.api.internal.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Integer> {
    boolean existsByEmail(String email);
    Optional<Users> findUserByEmail(String email);
}