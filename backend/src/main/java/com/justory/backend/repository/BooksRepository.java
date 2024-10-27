package com.justory.backend.repository;

import com.justory.backend.api.internal.Books;
import com.justory.backend.api.internal.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Books, Integer> {
    List<Books> findByTitle(String title);
    @Query("SELECT b FROM Books b JOIN b.authors a WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(a.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(a.lastName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Books> searchByTitleOrAuthor(String query);
    List<Books> findByCategoriesContaining(Categories category);
}
