package com.justory.backend.mapper;

import com.justory.backend.api.external.PlatformsDTO;
import com.justory.backend.api.internal.Platforms;
import com.justory.backend.service.FileUtils;
import org.springframework.stereotype.Component;

@Component
public class PlatformsMapper {

    public PlatformsDTO toDTO(Platforms platform) {
        return new PlatformsDTO()
                .setId(platform.getId())
                .setName(platform.getName())
                .setImg(FileUtils.readFileFromLocation(platform.getImg()));
    }

    public Platforms toEntity(PlatformsDTO dto) {
        String imgPath = "./uploads/platforms/" + dto.getName() + ".jpg";
        return new Platforms()
                .setName(dto.getName())
                .setImg(imgPath);
    }
}
