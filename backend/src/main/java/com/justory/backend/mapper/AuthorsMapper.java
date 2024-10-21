package com.justory.backend.mapper;


import com.justory.backend.api.external.AuthorsDTO;
import com.justory.backend.api.internal.Authors;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuthorsMapper {
    public AuthorsDTO toDTO(Authors author) {
        if (author == null) {
            return null;
        }
        return new AuthorsDTO()
                .setId(author.getId())
                .setFirstName(author.getFirstName())
                .setLastName(author.getLastName())
                .setBiography(author.getBiography());
    }

    public Authors toEntity(AuthorsDTO authorsDTO) {
        if (authorsDTO == null) {
            return null;
        }
        return new Authors()
                .setFirstName(authorsDTO.getFirstName())
                .setLastName(authorsDTO.getLastName())
                .setBiography(authorsDTO.getBiography());
    }

    public Set<AuthorsDTO> mapAuthorsToDTO(Set<Authors> authors) {
        if (authors == null) {
            return null;
        }
        return authors.stream()
                .map(this::toDTO)
                .collect(Collectors.toSet());
    }

    public Set<Authors> mapAuthorsToEntity(Set<AuthorsDTO> authorsDTO) {
        if (authorsDTO == null) {
            return null;
        }
        return authorsDTO.stream()
                .map(this::toEntity)
                .collect(Collectors.toSet());
    }
}