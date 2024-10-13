package com.justory.backend.mapper;

import com.justory.backend.api.external.CategoriesDTO;
import com.justory.backend.api.internal.Categories;

public class CategoriesMapper {
    public static CategoriesDTO toDTO(Categories category) {
        if (category == null) {
            return null;
        }
        return new CategoriesDTO()
                .setId(category.getId())
                .setName(category.getName());
    }
}