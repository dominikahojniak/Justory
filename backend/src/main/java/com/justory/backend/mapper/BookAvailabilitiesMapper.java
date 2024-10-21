package com.justory.backend.mapper;
import com.justory.backend.api.external.BookAvailabilitiesDTO;
import com.justory.backend.api.internal.*;
import com.justory.backend.repository.AccessTypesRepository;
import com.justory.backend.repository.BooksRepository;
import com.justory.backend.repository.FormatsRepository;
import com.justory.backend.repository.PlatformsRepository;
import com.justory.backend.service.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookAvailabilitiesMapper {
    @Autowired
    private BooksRepository booksRepository;

    @Autowired
    private PlatformsRepository platformsRepository;

    @Autowired
    private FormatsRepository formatsRepository;

    @Autowired
    private AccessTypesRepository accessTypesRepository;
    public BookAvailabilitiesDTO toDTO(BookAvailabilities availability) {
        return new BookAvailabilitiesDTO()
                .setBookId(availability.getBook().getId())
                .setPlatformName(availability.getPlatform().getName())
                .setPlatformLogo(FileUtils.readFileFromLocation(availability.getPlatform().getImg()))
                .setFormatName(availability.getFormat().getName())
                .setFormatLogo(FileUtils.readFileFromLocation(availability.getFormat().getImg()))
                .setAccessTypeName(availability.getAccessType().getName());
    }

    public BookAvailabilities toEntity(BookAvailabilitiesDTO dto) {
        Books book = booksRepository.findById(dto.getBookId()).orElseThrow(() -> new RuntimeException("Book not found"));
        Platforms platform = platformsRepository.findByName(dto.getPlatformName()).orElseThrow(() -> new RuntimeException("Platform not found"));
        Formats format = formatsRepository.findByName(dto.getFormatName()).orElseThrow(() -> new RuntimeException("Format not found"));
        AccessTypes accessType = accessTypesRepository.findByName(dto.getAccessTypeName()).orElseThrow(() -> new RuntimeException("Access type not found"));

        return new BookAvailabilities()
                .setBook(book)
                .setPlatform(platform)
                .setFormat(format)
                .setAccessType(accessType);
    }
}
