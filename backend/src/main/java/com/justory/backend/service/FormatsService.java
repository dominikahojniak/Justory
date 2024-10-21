package com.justory.backend.service;

import com.justory.backend.api.external.FormatsDTO;

import java.util.List;

public interface FormatsService {
    List<FormatsDTO> getAllFormats();
}
