package com.justory.backend.mapper;

import com.justory.backend.api.external.PublishersDTO;
import com.justory.backend.api.internal.Publishers;
import org.springframework.stereotype.Component;

@Component
public class PublishersMapper {

    public PublishersDTO toDTO(Publishers publisher) {
        if (publisher == null) {
            return null;
        }
        return new PublishersDTO()
                .setId(publisher.getId())
                .setName(publisher.getName())
                .setAddress(publisher.getAddress())
                .setWebsite(publisher.getWebsite());
    }

    public Publishers toEntity(PublishersDTO publisherDTO) {
        if (publisherDTO == null) {
            return null;
        }
        return new Publishers()
                .setName(publisherDTO.getName())
                .setAddress(publisherDTO.getAddress())
                .setWebsite(publisherDTO.getWebsite());
    }
}
