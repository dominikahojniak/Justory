package com.justory.backend.service;


import com.justory.backend.api.external.BookAvailabilitiesDTO;
import com.justory.backend.api.external.BooksDTO;
import com.justory.backend.api.internal.*;
import com.justory.backend.mapper.BookAvailabilitiesMapper;
import com.justory.backend.mapper.BooksMapper;
import com.justory.backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BooksServiceImpl implements BooksService {

    private final BooksRepository booksRepository;
    private final AuthorsRepository authorsRepository;
    private final CategoriesRepository categoriesRepository;
    private final BooksMapper booksMapper;
    private final PlatformsRepository platformsRepository;
    private final FormatsRepository formatsRepository;
    private final AccessTypesRepository accessTypesRepository;
    private final BookAvailabilitiesRepository bookAvailabilitiesRepository;
    private final BookAvailabilitiesMapper bookAvailabilitiesMapper;
    private final PublishersRepository publishersRepository;
    private final FileStorageService fileStorageService;


    @Override
    public List<BooksDTO> getAllBooks() {
        List<Books> booksList = booksRepository.findAll();
        return booksList.stream()
                .map(booksMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BooksDTO getBookByTitle(String title) {
        List<Books> booksList = booksRepository.findByTitle(title);
        if (booksList.isEmpty()) {
            return null;
        }

        Books book = booksList.get(0);  // Get the first result
        BooksDTO bookDTO = booksMapper.toDTO(book);
        Set<BookAvailabilities> availabilities = book.getAvailabilities();

        Set<BookAvailabilitiesDTO> availabilitiesDTO = availabilities.stream().map(availability -> {
            return new BookAvailabilitiesDTO()
                    .setPlatformName(availability.getPlatform().getName())
                    .setPlatformLogo(FileUtils.readFileFromLocation(availability.getPlatform().getImg()))
                    .setFormatName(availability.getFormat().getName())
                    .setFormatLogo(FileUtils.readFileFromLocation(availability.getFormat().getImg()))
                    .setAccessTypeName(availability.getAccessType().getName());
        }).collect(Collectors.toSet());

        bookDTO.setAvailabilities(availabilitiesDTO);

        return bookDTO;
    }

    @Override
    @Transactional
    public BooksDTO addBookWithAvailabilities(BooksDTO bookDTO, MultipartFile file) {
        Books book = booksMapper.toEntity(bookDTO);
        String filePath = fileStorageService.saveFile(file, bookDTO.getId());
        book.setImg(filePath);

        Set<Authors> authors = book.getAuthors().stream()
                .map(author -> authorsRepository.findByFirstNameAndLastName(
                                author.getFirstName(), author.getLastName())
                        .orElseGet(() -> authorsRepository.save(author)))
                .collect(Collectors.toSet());

        book.setAuthors(authors);

        Set<Categories> categories = book.getCategories().stream()
                .map(category -> categoriesRepository.findByName(category.getName())
                        .orElseGet(() -> categoriesRepository.save(category)))
                .collect(Collectors.toSet());

        book.setCategories(categories);

        Publishers publisher = publishersRepository.findByName(book.getPublisher().getName())
                .orElseGet(() -> publishersRepository.save(book.getPublisher()));
        book.setPublisher(publisher);

        Books savedBook = booksRepository.save(book);

        Set<BookAvailabilities> availabilities = bookDTO.getAvailabilities().stream()
                .map(availabilityDTO -> {
                    Platforms platform = platformsRepository.findByName(availabilityDTO.getPlatformName())
                            .orElseThrow(() -> new RuntimeException("Platform not found"));

                    Formats format = formatsRepository.findByName(availabilityDTO.getFormatName())
                            .orElseThrow(() -> new RuntimeException("Format not found"));

                    AccessTypes accessType = accessTypesRepository.findByName(availabilityDTO.getAccessTypeName())
                            .orElseThrow(() -> new RuntimeException("Access type not found"));

                    BookAvailabilities availability = new BookAvailabilities()
                            .setBook(savedBook)
                            .setPlatform(platform)
                            .setFormat(format)
                            .setAccessType(accessType);

                    return bookAvailabilitiesRepository.save(availability);
                }).collect(Collectors.toSet());

        savedBook.setAvailabilities(availabilities);

        return booksMapper.toDTO(savedBook);
    }

    @Override
    @Transactional
    public BooksDTO addBook(BooksDTO bookDTO) {
        Books book = booksMapper.toEntity(bookDTO);

        Set<Authors> authors = book.getAuthors().stream()
                .map(author -> authorsRepository.findByFirstNameAndLastName(
                                author.getFirstName(), author.getLastName())
                        .orElseGet(() -> authorsRepository.save(author)))
                .collect(Collectors.toSet());

        book.setAuthors(authors);

        Set<Categories> categories = book.getCategories().stream()
                .map(category -> categoriesRepository.findByName(category.getName())
                        .orElseGet(() -> categoriesRepository.save(category)))
                .collect(Collectors.toSet());

        book.setCategories(categories);

        Books savedBook = booksRepository.save(book);
        return booksMapper.toDTO(savedBook);
    }

}
