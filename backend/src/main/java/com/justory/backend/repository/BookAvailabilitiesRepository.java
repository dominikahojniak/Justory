package com.justory.backend.repository;

import com.justory.backend.api.internal.BookAvailabilities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookAvailabilitiesRepository extends JpaRepository<BookAvailabilities, Integer> {
}
