package com.justory.backend.service;


import com.justory.backend.api.external.BookAvailabilitiesDTO;
import com.justory.backend.api.external.BooksDTO;
import com.justory.backend.api.internal.*;
import com.justory.backend.mapper.BookAvailabilitiesMapper;
import com.justory.backend.mapper.BooksMapper;
import com.justory.backend.repository.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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

        Books book = booksList.get(0);
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
                .map(author -> {
                    String formattedFirstName = formatString(author.getFirstName().trim());
                    String formattedLastName = formatString(author.getLastName().trim());
                    author.setFirstName(formattedFirstName);
                    author.setLastName(formattedLastName);
                    return authorsRepository.findByFirstNameAndLastName(formattedFirstName, formattedLastName)
                            .orElseGet(() -> authorsRepository.save(author));
                })
                .collect(Collectors.toSet());

        book.setAuthors(authors);

        Set<Categories> categories = book.getCategories().stream()
                .map(category -> {
                    String formattedName = formatString(category.getName().trim());
                    category.setName(formattedName);
                    return categoriesRepository.findByName(formattedName)
                            .orElseGet(() -> categoriesRepository.save(category));
                })
                .collect(Collectors.toSet());

        book.setCategories(categories);

        String formattedPublisherName = formatString(book.getPublisher().getName().trim());
        book.getPublisher().setName(formattedPublisherName);

        Publishers publisher = publishersRepository.findByName(formattedPublisherName)
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
    public void deleteBookById(Integer bookId) {
        Books book = null;
        try {
            book = booksRepository.findById(bookId)
                    .orElseThrow(() -> new Exception("Book with ID: " + bookId + " not founded."));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        book.getUserToReadListEntries().size();

        booksRepository.delete(book);
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
    @Override
    public List<BooksDTO> searchBooks(String query) {
        List<Books> searchResults = booksRepository.searchByTitleOrAuthor(query);
        return searchResults.stream()
                .map(booksMapper::toDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<BooksDTO> findBooksByCategory(String categoryName) {
        Categories category = categoriesRepository.findByName(categoryName)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        List<Books> books = booksRepository.findByCategoriesContaining(category);
        return books.stream().map(booksMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BooksDTO updateBook(Integer bookId, BooksDTO bookDTO, MultipartFile file) {
        Books book = booksRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        book.setTitle(bookDTO.getTitle())
                .setLanguage(bookDTO.getLanguage())
                .setDescription(bookDTO.getDescription())
                .setISBN(bookDTO.getISBN())
                .setDate(bookDTO.getDate());

        if (file != null && !file.isEmpty()) {
            String filePath = fileStorageService.saveFile(file, bookDTO.getId());
            book.setImg(filePath);
        }

        Set<Authors> authors = bookDTO.getAuthors().stream()
                .map(authorDTO -> authorsRepository.findByFirstNameAndLastName(
                                authorDTO.getFirstName(), authorDTO.getLastName())
                        .orElseGet(() -> authorsRepository.save(
                                new Authors().setFirstName(formatString(authorDTO.getFirstName()))
                                        .setLastName(formatString(authorDTO.getLastName())))))
                .collect(Collectors.toSet());
        book.setAuthors(authors);

        Set<Categories> categories = bookDTO.getCategories().stream()
                .map(categoryDTO -> categoriesRepository.findByName(categoryDTO.getName())
                        .orElseGet(() -> categoriesRepository.save(new Categories().setName(formatString(categoryDTO.getName())))))
                .collect(Collectors.toSet());
        book.setCategories(categories);

        Publishers publisher = publishersRepository.findByName(bookDTO.getPublisher().getName())
                .orElseGet(() -> publishersRepository.save(new Publishers().setName(formatString(bookDTO.getPublisher().getName()))));
        book.setPublisher(publisher);

        Set<BookAvailabilities> existingAvailabilities = book.getAvailabilities();
        Set<BookAvailabilities> updatedAvailabilities = new HashSet<>();

        for (BookAvailabilitiesDTO availabilityDTO : bookDTO.getAvailabilities()) {
            Platforms platform = platformsRepository.findByName(availabilityDTO.getPlatformName())
                    .orElseThrow(() -> new RuntimeException("Platform not found"));
            Formats format = formatsRepository.findByName(availabilityDTO.getFormatName())
                    .orElseThrow(() -> new RuntimeException("Format not found"));
            AccessTypes accessType = accessTypesRepository.findByName(availabilityDTO.getAccessTypeName())
                    .orElseThrow(() -> new RuntimeException("Access type not found"));

            Optional<BookAvailabilities> existing = existingAvailabilities.stream()
                    .filter(a -> a.getPlatform().equals(platform)
                            && a.getFormat().equals(format)
                            && a.getAccessType().equals(accessType))
                    .findFirst();

            if (existing.isPresent()) {
                updatedAvailabilities.add(existing.get());
            } else {
                BookAvailabilities newAvailability = new BookAvailabilities()
                        .setBook(book)
                        .setPlatform(platform)
                        .setFormat(format)
                        .setAccessType(accessType);

                updatedAvailabilities.add(newAvailability);
            }
        }

        book.getAvailabilities().clear();
        book.getAvailabilities().addAll(updatedAvailabilities);

        Books updatedBook = booksRepository.save(book);
        return booksMapper.toDTO(updatedBook);
    }
    private String formatString(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
}
