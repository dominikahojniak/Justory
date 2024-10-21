package com.justory.backend.repository;

import com.justory.backend.api.internal.Platforms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlatformsRepository extends JpaRepository<Platforms, Integer> {
    Optional<Platforms> findByName(String name);
}
