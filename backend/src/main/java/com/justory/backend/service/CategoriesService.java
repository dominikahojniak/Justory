package com.justory.backend.service;

import com.justory.backend.api.external.CategoriesDTO;
import com.justory.backend.api.internal.Categories;

import java.util.List;

public interface CategoriesService {
    List<CategoriesDTO> getAllCategories();
    Categories findByName(String categoryName);
}
