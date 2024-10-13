package com.justory.backend.mapper;


import com.justory.backend.api.external.AuthorsDTO;
import com.justory.backend.api.internal.Authors;

public class AuthorsMapper {
    public static AuthorsDTO toDTO(Authors author) {
        if (author == null) {
            return null;
        }
        return new AuthorsDTO()
                .setId(author.getId())
                .setFirstName(author.getFirstName())
                .setLastName(author.getLastName())
                .setBiography(author.getBiography());
    }
}