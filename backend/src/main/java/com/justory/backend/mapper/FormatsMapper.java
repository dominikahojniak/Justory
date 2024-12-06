package com.justory.backend.mapper;
import com.justory.backend.api.external.FormatsDTO;
import com.justory.backend.api.internal.Formats;
import com.justory.backend.service.FileUtils;
import org.springframework.stereotype.Component;

@Component
public class FormatsMapper {

    public FormatsDTO toDTO(Formats format) {
        return new FormatsDTO()
                .setId(format.getId())
                .setName(format.getName())
                .setImg(FileUtils.readFileFromLocation(format.getImg()));
    }

    public Formats toEntity(FormatsDTO dto) {
        String imgPath = "/app/uploads/formats/" + dto.getName() + ".jpg";
        return new Formats()
                .setName(dto.getName())
                .setImg(imgPath);
    }
}