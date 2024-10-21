package com.justory.backend.service;

import com.justory.backend.api.external.FormatsDTO;
import com.justory.backend.mapper.FormatsMapper;
import com.justory.backend.repository.FormatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FormatsServiceImpl implements FormatsService {

    private final FormatsRepository formatsRepository;
    private final FormatsMapper formatsMapper;

    @Override
    public List<FormatsDTO> getAllFormats() {
        return formatsRepository.findAll().stream()
                .map(formatsMapper::toDTO)
                .collect(Collectors.toList());
    }
}