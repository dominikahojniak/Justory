package com.justory.backend.repository;

import com.justory.backend.api.internal.Authors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorsRepository extends JpaRepository<Authors, Integer> {
    Optional<Authors> findByFirstNameAndLastName(String firstName, String lastName);
}