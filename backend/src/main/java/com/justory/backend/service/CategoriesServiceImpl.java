package com.justory.backend.service;

import com.justory.backend.api.external.CategoriesDTO;
import com.justory.backend.api.internal.Categories;
import com.justory.backend.mapper.CategoriesMapper;
import com.justory.backend.repository.CategoriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriesServiceImpl implements CategoriesService {

    private final CategoriesRepository categoriesRepository;
    private final CategoriesMapper categoriesMapper;

    @Override
    public List<CategoriesDTO> getAllCategories() {
        return categoriesRepository.findAll().stream()
                .map(categoriesMapper::toDTO)
                .collect(Collectors.toList());
    }
    @Override
    public Categories findByName(String categoryName) {
        return categoriesRepository.findByName(categoryName)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }
}
