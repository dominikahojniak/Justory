package com.justory.backend.service;

import com.justory.backend.api.external.PlatformsDTO;
import com.justory.backend.mapper.PlatformsMapper;
import com.justory.backend.repository.PlatformsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlatformsServiceImpl implements PlatformsService {

    private final PlatformsRepository platformsRepository;
    private final PlatformsMapper platformsMapper;

    @Override
    public List<PlatformsDTO> getAllPlatforms() {
        return platformsRepository.findAll().stream()
                .map(platformsMapper::toDTO)
                .collect(Collectors.toList());
    }
}
