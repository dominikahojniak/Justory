package com.justory.backend.service;

import com.justory.backend.api.external.AccessTypesDTO;
import com.justory.backend.mapper.AccessTypesMapper;
import com.justory.backend.repository.AccessTypesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccessTypesServiceImpl implements AccessTypesService {

    private final AccessTypesRepository accessTypesRepository;
    private final AccessTypesMapper accessTypesMapper;

    @Override
    public List<AccessTypesDTO> getAllAccessTypes() {
        return accessTypesRepository.findAll().stream()
                .map(accessTypesMapper::toDTO)
                .collect(Collectors.toList());
    }
}