package com.justory.backend.mapper;


import com.justory.backend.api.external.AuthorsDTO;
import com.justory.backend.api.external.BooksDTO;
import com.justory.backend.api.external.CategoriesDTO;
import com.justory.backend.api.internal.Books;
import com.justory.backend.api.internal.Authors;
import com.justory.backend.api.internal.Categories;
import com.justory.backend.service.FileUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BooksMapper {

    public BooksDTO toDTO(Books book) {
        return new BooksDTO()
                .setId(book.getId())
                .setTitle(book.getTitle())
                .setLanguage(book.getLanguage())
                .setDescription(book.getDescription())
                .setISBN(book.getISBN())
                .setDate(book.getDate())
                .setImg(FileUtils.readFileFromLocation(book.getImg()))
                .setCategories(mapCategoriesToDTO(book.getCategories()))
                .setAuthors(mapAuthorsToDTO(book.getAuthors()));
    }

    private Set<CategoriesDTO> mapCategoriesToDTO(Set<Categories> categories) {
        if (categories == null) {
            return null;
        }
        return categories.stream()
                .map(CategoriesMapper::toDTO)
                .collect(Collectors.toSet());
    }

    private Set<AuthorsDTO> mapAuthorsToDTO(Set<Authors> authors) {
        if (authors == null) {
            return null;
        }
        return authors.stream()
                .map(AuthorsMapper::toDTO)
                .collect(Collectors.toSet());
    }
}