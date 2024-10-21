package com.justory.backend.controller;

import com.justory.backend.api.external.AccessTypesDTO;
import com.justory.backend.api.internal.AccessTypes;
import com.justory.backend.repository.AccessTypesRepository;
import com.justory.backend.service.AccessTypesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/access-types")
@RequiredArgsConstructor
public class AccessTypesController {

    private final AccessTypesService accessTypesService;

    @GetMapping
    public List<AccessTypesDTO> getAllAccessTypes() {
        return accessTypesService.getAllAccessTypes();
    }
}