package com.justory.backend;

import com.justory.backend.api.external.BooksDTO;
import com.justory.backend.api.internal.*;
import com.justory.backend.mapper.BooksMapper;
import com.justory.backend.repository.UserToReadListRepository;
import com.justory.backend.service.UserToReadListServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserToReadListServiceTest {
    @Mock
    private UserToReadListRepository userToReadListRepository;

    @Mock
    private BooksMapper booksMapper;

    @InjectMocks
    private UserToReadListServiceImpl userToReadListService;

    @Test
    void getUserToReadBooks_ShouldReturnListOfBooks_WhenBooksExist() {
        Integer userId = 1;

        UserToReadList entry1 = new UserToReadList();
        entry1.setUser(new Users().setId(userId));
        entry1.setBook(new Books().setId(1).setTitle("Book 1"));

        UserToReadList entry2 = new UserToReadList();
        entry2.setUser(new Users().setId(userId));
        entry2.setBook(new Books().setId(2).setTitle("Book 2"));

        List<UserToReadList> userToReadList = Arrays.asList(entry1, entry2);

        BooksDTO bookDTO1 = new BooksDTO();
        bookDTO1.setId(1);
        bookDTO1.setTitle("Book 1");

        BooksDTO bookDTO2 = new BooksDTO();
        bookDTO2.setId(2);
        bookDTO2.setTitle("Book 2");

        when(userToReadListRepository.findByUserId(userId)).thenReturn(userToReadList);
        when(booksMapper.toDTO(entry1.getBook())).thenReturn(bookDTO1);
        when(booksMapper.toDTO(entry2.getBook())).thenReturn(bookDTO2);

        List<BooksDTO> result = userToReadListService.getUserToReadBooks(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(bookDTO1, result.get(0));
        assertEquals(bookDTO2, result.get(1));

        verify(userToReadListRepository).findByUserId(userId);
        verify(booksMapper).toDTO(entry1.getBook());
        verify(booksMapper).toDTO(entry2.getBook());
    }

    @Test
    void getUserToReadBooks_ShouldReturnEmptyList_WhenNoBooksExist() {
        Integer userId = 1;

        when(userToReadListRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        List<BooksDTO> result = userToReadListService.getUserToReadBooks(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userToReadListRepository).findByUserId(userId);
        verifyNoInteractions(booksMapper);
    }

    @Test
    void addBookToUserToReadList_ShouldAddBook_WhenBookNotInList() throws Exception {
        Integer userId = 1;
        Integer bookId = 1;

        when(userToReadListRepository.findByUserIdAndBookId(userId, bookId))
                .thenReturn(Optional.empty());

        userToReadListService.addBookToUserToReadList(userId, bookId);

        ArgumentCaptor<UserToReadList> captor = ArgumentCaptor.forClass(UserToReadList.class);
        verify(userToReadListRepository).save(captor.capture());

        UserToReadList savedEntry = captor.getValue();
        assertNotNull(savedEntry);
        assertEquals(userId, savedEntry.getUser().getId());
        assertEquals(bookId, savedEntry.getBook().getId());
        assertEquals(LocalDate.now(), savedEntry.getDateAdded());
    }

    @Test
    void addBookToUserToReadList_ShouldThrowException_WhenBookAlreadyInList() {
        Integer userId = 1;
        Integer bookId = 1;

        UserToReadList existingEntry = new UserToReadList();
        existingEntry.setUser(new Users().setId(userId));
        existingEntry.setBook(new Books().setId(bookId));

        when(userToReadListRepository.findByUserIdAndBookId(userId, bookId))
                .thenReturn(Optional.of(existingEntry));

        Exception exception = assertThrows(Exception.class, () -> {
            userToReadListService.addBookToUserToReadList(userId, bookId);
        });

        assertEquals("Book is already in the user's to-read list", exception.getMessage());

        verify(userToReadListRepository, never()).save(any(UserToReadList.class));
    }

    @Test
    void removeBookFromUserToReadList_ShouldRemoveBook_WhenBookExists() {
        Integer userId = 1;
        Integer bookId = 1;

        UserToReadList existingEntry = new UserToReadList();
        existingEntry.setUser(new Users().setId(userId));
        existingEntry.setBook(new Books().setId(bookId));

        when(userToReadListRepository.findByUserIdAndBookId(userId, bookId))
                .thenReturn(Optional.of(existingEntry));

        userToReadListService.removeBookFromUserToReadList(userId, bookId);

        verify(userToReadListRepository).deleteByUserIdAndBookId(userId, bookId);
    }

    @Test
    void removeBookFromUserToReadList_ShouldNotThrowException_WhenBookDoesNotExist() {
        Integer userId = 1;
        Integer bookId = 1;

        when(userToReadListRepository.findByUserIdAndBookId(userId, bookId))
                .thenReturn(Optional.empty());

        userToReadListService.removeBookFromUserToReadList(userId, bookId);

        verify(userToReadListRepository, never()).deleteByUserIdAndBookId(userId, bookId);
    }
}
