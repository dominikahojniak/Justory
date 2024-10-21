package com.justory.backend.repository;

import com.justory.backend.api.internal.Publishers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublishersRepository extends JpaRepository<Publishers, Integer> {
    Optional<Publishers> findByName(String name);
}
