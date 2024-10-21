package com.justory.backend.repository;

import com.justory.backend.api.internal.Formats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FormatsRepository extends JpaRepository<Formats, Integer> {
    Optional<Formats> findByName(String name);
}
