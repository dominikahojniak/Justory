package com.justory.backend;

import com.justory.backend.api.external.BookAvailabilitiesDTO;
import com.justory.backend.api.external.BooksDTO;
import com.justory.backend.api.internal.*;
import com.justory.backend.mapper.BookAvailabilitiesMapper;
import com.justory.backend.mapper.BooksMapper;
import com.justory.backend.repository.*;
import com.justory.backend.service.BooksServiceImpl;
import com.justory.backend.service.FileStorageService;
import com.justory.backend.service.FileUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BooksServiceTest {
    @Mock
    private BooksRepository booksRepository;

    @Mock
    private CategoriesRepository categoriesRepository;

    @Mock
    private BooksMapper booksMapper;

    @Mock
    private PlatformsRepository platformsRepository;

    @Mock
    private FormatsRepository formatsRepository;

    @Mock
    private AccessTypesRepository accessTypesRepository;

    @Mock
    private BookAvailabilitiesRepository bookAvailabilitiesRepository;

    @Mock
    private PublishersRepository publishersRepository;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private BooksServiceImpl booksService;

    @Test
    void getAllBooks_ShouldReturnListOfBooksDTO_WhenBooksExist() {
        Books book1 = new Books().setId(1).setTitle("Book One");
        Books book2 = new Books().setId(2).setTitle("Book Two");
        List<Books> booksList = Arrays.asList(book1, book2);

        BooksDTO dto1 = new BooksDTO();
        dto1.setId(1);
        dto1.setTitle("Book One DTO");

        BooksDTO dto2 = new BooksDTO();
        dto2.setId(2);
        dto2.setTitle("Book Two DTO");

        when(booksRepository.findAll()).thenReturn(booksList);
        when(booksMapper.toDTO(book1)).thenReturn(dto1);
        when(booksMapper.toDTO(book2)).thenReturn(dto2);

        List<BooksDTO> result = booksService.getAllBooks();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
    }

    @Test
    void getAllBooks_ShouldReturnEmptyList_WhenNoBooksFound() {
        when(booksRepository.findAll()).thenReturn(Collections.emptyList());

        List<BooksDTO> result = booksService.getAllBooks();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getBookByTitle_ShouldReturnBookDTO_WhenBookExists() {
        String title = "Some Book";
        Books book = new Books().setId(1).setTitle(title);

        Platforms platform = new Platforms().setId(1).setName("Kindle").setImg("kindle_logo.png");
        Formats format = new Formats().setId(1).setName("Ebook").setImg("ebook_logo.png");
        AccessTypes accessType = new AccessTypes().setId(1).setName("Free");

        BookAvailabilities availability = new BookAvailabilities()
                .setBook(book)
                .setPlatform(platform)
                .setFormat(format)
                .setAccessType(accessType);
        book.setAvailabilities(Set.of(availability));

        BooksDTO bookDTO = new BooksDTO();
        bookDTO.setId(1);
        bookDTO.setTitle(title);

        when(booksRepository.findByTitle(title)).thenReturn(List.of(book));
        when(booksMapper.toDTO(book)).thenReturn(bookDTO);

        try (MockedStatic<FileUtils> utilities = mockStatic(FileUtils.class)) {
            utilities.when(() -> FileUtils.readFileFromLocation("kindle_logo.png"))
                    .thenReturn(new byte[]{1, 2, 3});
            utilities.when(() -> FileUtils.readFileFromLocation("ebook_logo.png"))
                    .thenReturn(new byte[]{4, 5, 6});

            BooksDTO result = booksService.getBookByTitle(title);

            assertNotNull(result);
            assertEquals(1, result.getId());
            assertNotNull(result.getAvailabilities());
            assertEquals(1, result.getAvailabilities().size());

            BookAvailabilitiesDTO availabilityDTO = result.getAvailabilities().iterator().next();
            assertEquals("Kindle", availabilityDTO.getPlatformName());
            assertArrayEquals(new byte[]{1, 2, 3}, availabilityDTO.getPlatformLogo());
            assertEquals("Ebook", availabilityDTO.getFormatName());
            assertArrayEquals(new byte[]{4, 5, 6}, availabilityDTO.getFormatLogo());
            assertEquals("Free", availabilityDTO.getAccessTypeName());
        }
    }

    @Test
    void getBookByTitle_ShouldReturnNull_WhenNoBookWithTitle() {
        String title = "NonExisting";
        when(booksRepository.findByTitle(title)).thenReturn(Collections.emptyList());

        BooksDTO result = booksService.getBookByTitle(title);

        assertNull(result);
    }

    @Test
    void addBookWithAvailabilities_ShouldSaveBookAndAvailabilities_WhenDataIsValid() {
        BooksDTO bookDTO = new BooksDTO();
        bookDTO.setId(10);
        bookDTO.setTitle("New Book");
        bookDTO.setPublisher(new com.justory.backend.api.external.PublishersDTO().setName("Test Publisher"));

        BookAvailabilitiesDTO availabilityDTO = new BookAvailabilitiesDTO()
                .setPlatformName("Kindle")
                .setFormatName("Ebook")
                .setAccessTypeName("Free");
        bookDTO.setAvailabilities(Set.of(availabilityDTO));

        Books bookEntity = new Books().setId(10).setTitle("New Book");
        bookEntity.setAuthors(new HashSet<>());
        bookEntity.setCategories(new HashSet<>());
        bookEntity.setPublisher(new Publishers().setName("Test Publisher"));

        when(booksMapper.toEntity(bookDTO)).thenReturn(bookEntity);

        MultipartFile file = mock(MultipartFile.class);
        when(fileStorageService.saveFile(file, 10)).thenReturn("path/to/img.png");

        when(publishersRepository.findByName("Test publisher")).thenReturn(Optional.empty());
        when(publishersRepository.save(any(Publishers.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(booksRepository.save(any(Books.class))).thenAnswer(invocation -> {
            Books saved = invocation.getArgument(0);
            saved.setId(10);
            return saved;
        });

        when(platformsRepository.findByName("Kindle")).thenReturn(Optional.of(new Platforms().setName("Kindle")));
        when(formatsRepository.findByName("Ebook")).thenReturn(Optional.of(new Formats().setName("Ebook")));
        when(accessTypesRepository.findByName("Free")).thenReturn(Optional.of(new AccessTypes().setName("Free")));

        when(bookAvailabilitiesRepository.save(any(BookAvailabilities.class))).thenAnswer(invocation -> {
            BookAvailabilities ba = invocation.getArgument(0);
            ba.setId(100);
            return ba;
        });

        BooksDTO savedDTO = new BooksDTO();
        savedDTO.setId(10);
        savedDTO.setTitle("New Book");
        when(booksMapper.toDTO(any(Books.class))).thenReturn(savedDTO);

        BooksDTO result = booksService.addBookWithAvailabilities(bookDTO, file);

        assertNotNull(result);
        assertEquals(10, result.getId());

        verify(booksMapper).toEntity(bookDTO);
        verify(fileStorageService).saveFile(file, 10);
        verify(booksRepository, atLeastOnce()).save(any(Books.class));
        verify(bookAvailabilitiesRepository, atLeastOnce()).save(any(BookAvailabilities.class));
        verify(booksMapper).toDTO(any(Books.class));
    }

    @Test
    void addBookWithAvailabilities_ShouldThrowException_WhenPlatformNotFound() {
        BooksDTO bookDTO = new BooksDTO();
        bookDTO.setId(10);
        bookDTO.setTitle("New Book");
        bookDTO.setPublisher(new com.justory.backend.api.external.PublishersDTO().setName("Test Publisher"));

        BookAvailabilitiesDTO availabilityDTO = new BookAvailabilitiesDTO()
                .setPlatformName("UnknownPlatform")
                .setFormatName("Ebook")
                .setAccessTypeName("Free");
        bookDTO.setAvailabilities(Set.of(availabilityDTO));

        Books bookEntity = new Books().setId(10).setTitle("New Book");
        bookEntity.setAuthors(new HashSet<>());
        bookEntity.setCategories(new HashSet<>());
        bookEntity.setPublisher(new Publishers().setName("Test Publisher"));

        when(booksMapper.toEntity(bookDTO)).thenReturn(bookEntity);
        MultipartFile file = mock(MultipartFile.class);

        when(publishersRepository.save(any(Publishers.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(booksRepository.save(any(Books.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(platformsRepository.findByName("UnknownPlatform")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> booksService.addBookWithAvailabilities(bookDTO, file));
    }

    @Test
    void deleteBookById_ShouldDeleteBook_WhenBookExists() throws Exception {
        Integer bookId = 1;
        Books book = new Books().setId(bookId).setTitle("Delete Me");
        book.setUserToReadListEntries(new HashSet<>());

        when(booksRepository.findById(bookId)).thenReturn(Optional.of(book));

        booksService.deleteBookById(bookId);

        verify(booksRepository).findById(bookId);
        verify(booksRepository).delete(book);
    }

    @Test
    void deleteBookById_ShouldThrowException_WhenBookNotFound() {
        Integer bookId = 999;
        when(booksRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> booksService.deleteBookById(bookId));
    }

    @Test
    void searchBooks_ShouldReturnList_WhenMatchesFound() {
        String query = "keyword";
        Books book1 = new Books().setId(1).setTitle("keyword Book");
        Books book2 = new Books().setId(2).setTitle("Another keyword");

        when(booksRepository.searchByTitleOrAuthor(query)).thenReturn(Arrays.asList(book1, book2));

        BooksDTO dto1 = new BooksDTO();
        dto1.setId(1);
        dto1.setTitle("keyword Book DTO");

        BooksDTO dto2 = new BooksDTO();
        dto2.setId(2);
        dto2.setTitle("Another keyword DTO");

        when(booksMapper.toDTO(book1)).thenReturn(dto1);
        when(booksMapper.toDTO(book2)).thenReturn(dto2);

        List<BooksDTO> result = booksService.searchBooks(query);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
    }

    @Test
    void searchBooks_ShouldReturnEmptyList_WhenNoMatchesFound() {
        String query = "none";
        when(booksRepository.searchByTitleOrAuthor(query)).thenReturn(Collections.emptyList());

        List<BooksDTO> result = booksService.searchBooks(query);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findBooksByCategory_ShouldReturnListOfBooksDTO_WhenCategoryExists() {
        String categoryName = "Fiction";
        Categories category = new Categories().setName("Fiction");
        Books book = new Books().setId(1).setTitle("Fiction Book");
        when(categoriesRepository.findByName("Fiction")).thenReturn(Optional.of(category));
        when(booksRepository.findByCategoriesContaining(category)).thenReturn(List.of(book));

        BooksDTO dto = new BooksDTO();
        dto.setId(1);
        dto.setTitle("Fiction Book DTO");
        when(booksMapper.toDTO(book)).thenReturn(dto);

        List<BooksDTO> result = booksService.findBooksByCategory(categoryName);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void findBooksByCategory_ShouldThrowException_WhenCategoryNotFound() {
        String categoryName = "UnknownCategory";
        when(categoriesRepository.findByName("Unknowncategory")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> booksService.findBooksByCategory(categoryName));
    }

    @Test
    void updateBook_ShouldUpdateBook_WhenBookExists() {
        Integer bookId = 10;
        Books existingBook = new Books().setId(bookId).setTitle("Old Title");
        existingBook.setAvailabilities(new HashSet<>());

        when(booksRepository.findById(bookId)).thenReturn(Optional.of(existingBook));

        BooksDTO bookDTO = new BooksDTO();
        bookDTO.setId(10);
        bookDTO.setTitle("New Title");
        bookDTO.setAuthors(Collections.emptySet());
        bookDTO.setCategories(Collections.emptySet());
        bookDTO.setPublisher(new com.justory.backend.api.external.PublishersDTO().setName("New Publisher"));
        bookDTO.setAvailabilities(Collections.emptySet());

        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        when(publishersRepository.findByName("New Publisher")).thenReturn(Optional.empty());
        when(publishersRepository.save(any(Publishers.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(booksRepository.save(any(Books.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BooksDTO updatedDTO = new BooksDTO();
        updatedDTO.setId(10);
        updatedDTO.setTitle("New Title");
        when(booksMapper.toDTO(any(Books.class))).thenReturn(updatedDTO);

        BooksDTO result = booksService.updateBook(bookId, bookDTO, file);

        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        verify(booksRepository).findById(bookId);
        verify(booksRepository).save(any(Books.class));
        verify(booksMapper).toDTO(any(Books.class));
    }

    @Test
    void updateBook_ShouldThrowException_WhenBookNotFound() {
        Integer bookId = 999;
        BooksDTO bookDTO = new BooksDTO();
        bookDTO.setTitle("New Title");

        when(booksRepository.findById(bookId)).thenReturn(Optional.empty());

        MultipartFile file = mock(MultipartFile.class);

        assertThrows(RuntimeException.class, () -> booksService.updateBook(bookId, bookDTO, file));
    }
}
