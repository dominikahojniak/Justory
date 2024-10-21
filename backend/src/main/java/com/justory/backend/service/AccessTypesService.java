package com.justory.backend.service;

import com.justory.backend.api.external.AccessTypesDTO;

import java.util.List;

public interface AccessTypesService {
    List<AccessTypesDTO> getAllAccessTypes();
}
