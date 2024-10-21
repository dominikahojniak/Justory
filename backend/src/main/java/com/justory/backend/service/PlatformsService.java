package com.justory.backend.service;

import com.justory.backend.api.external.PlatformsDTO;

import java.util.List;

public interface PlatformsService {
    List<PlatformsDTO> getAllPlatforms();
}
