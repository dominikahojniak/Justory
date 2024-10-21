package com.justory.backend.mapper;

import com.justory.backend.api.external.CategoriesDTO;
import com.justory.backend.api.internal.Categories;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CategoriesMapper {
    public CategoriesDTO toDTO(Categories category) {
        if (category == null) {
            return null;
        }
        return new CategoriesDTO()
                .setId(category.getId())
                .setName(category.getName());
    }

    public Categories toEntity(CategoriesDTO categoriesDTO) {
        if (categoriesDTO == null) {
            return null;
        }
        return new Categories()
                .setName(categoriesDTO.getName());
    }

    public Set<CategoriesDTO> mapCategoriesToDTO(Set<Categories> categories) {
        if (categories == null) {
            return null;
        }
        return categories.stream()
                .map(this::toDTO)
                .collect(Collectors.toSet());
    }

    public Set<Categories> mapCategoriesToEntity(Set<CategoriesDTO> categoriesDTO) {
        if (categoriesDTO == null) {
            return null;
        }
        return categoriesDTO.stream()
                .map(this::toEntity)
                .collect(Collectors.toSet());
    }
}