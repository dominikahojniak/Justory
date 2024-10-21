package com.justory.backend.mapper;

import com.justory.backend.api.external.AccessTypesDTO;
import com.justory.backend.api.internal.AccessTypes;
import org.springframework.stereotype.Component;

@Component
public class AccessTypesMapper {

    public AccessTypesDTO toDTO(AccessTypes accessType) {
        return new AccessTypesDTO()
                .setId(accessType.getId())
                .setName(accessType.getName());
    }

    public AccessTypes toEntity(AccessTypesDTO dto) {
        return new AccessTypes()
                .setName(dto.getName());
    }
}