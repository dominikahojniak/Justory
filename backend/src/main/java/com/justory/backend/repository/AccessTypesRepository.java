package com.justory.backend.repository;

import com.justory.backend.api.internal.AccessTypes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessTypesRepository extends JpaRepository<AccessTypes, Integer> {
    Optional<AccessTypes> findByName(String name);
}
