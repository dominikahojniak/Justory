package com.justory.backend.controller;

import com.justory.backend.api.external.FormatsDTO;

import com.justory.backend.service.FormatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/formats")
@RequiredArgsConstructor
public class FormatsController {

    private final FormatsService formatsService;

    @GetMapping
    public List<FormatsDTO> getAllFormats() {
        return formatsService.getAllFormats();
    }
}