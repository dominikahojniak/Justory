package com.justory.backend;

import com.justory.backend.api.external.BookRatingDTO;
import com.justory.backend.api.external.BooksDTO;
import com.justory.backend.api.external.UsersDTO;
import com.justory.backend.api.internal.*;
import com.justory.backend.mapper.BookRatingMapper;
import com.justory.backend.repository.BookRatingRepository;
import com.justory.backend.service.BookRatingServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookRatingServiceTest {
    @Mock
    private BookRatingRepository bookRatingRepository;

    @Mock
    private BookRatingMapper bookRatingMapper;

    @InjectMocks
    private BookRatingServiceImpl bookRatingService;

    @Test
    void addRating_ShouldAddRating_WhenRatingDoesNotExist() throws Exception {
        Integer userId = 1;
        Integer bookId = 1;
        int rating = 5;

        when(bookRatingRepository.findByUserIdAndBookId(userId, bookId))
                .thenReturn(Optional.empty());

        bookRatingService.addRating(userId, bookId, rating);

        ArgumentCaptor<BookRating> ratingCaptor = ArgumentCaptor.forClass(BookRating.class);
        verify(bookRatingRepository).save(ratingCaptor.capture());
        BookRating savedRating = ratingCaptor.getValue();

        assertNotNull(savedRating);
        assertEquals(userId, savedRating.getUser().getId());
        assertEquals(bookId, savedRating.getBook().getId());
        assertEquals(rating, savedRating.getRating());
    }

    @Test
    void addRating_ShouldThrowException_WhenRatingAlreadyExists() {
        Integer userId = 1;
        Integer bookId = 1;
        int rating = 5;

        BookRating existingRating = new BookRating()
                .setUser(new Users().setId(userId))
                .setBook(new Books().setId(bookId))
                .setRating(rating);

        when(bookRatingRepository.findByUserIdAndBookId(userId, bookId))
                .thenReturn(Optional.of(existingRating));

        Exception exception = assertThrows(Exception.class, () -> {
            bookRatingService.addRating(userId, bookId, rating);
        });

        assertEquals("Book is already rated by user", exception.getMessage());

        verify(bookRatingRepository, never()).save(any(BookRating.class));
    }

    @Test
    void getRatingsByUserId_ShouldReturnListOfRatings_WhenRatingsExist() {
        Integer userId = 1;

        BookRating rating1 = new BookRating()
                .setId(1)
                .setRating(5)
                .setUser(new Users().setId(userId))
                .setBook(new Books().setId(101));

        BookRating rating2 = new BookRating()
                .setId(2)
                .setRating(4)
                .setUser(new Users().setId(userId))
                .setBook(new Books().setId(102));

        List<BookRating> ratings = Arrays.asList(rating1, rating2);

        BooksDTO bookDTO1 = new BooksDTO();
        bookDTO1.setId(101);

        BooksDTO bookDTO2 = new BooksDTO();
        bookDTO2.setId(102);

        BookRatingDTO dto1 = new BookRatingDTO();
        dto1.setId(1);
        dto1.setRating(5);
        dto1.setUser(new UsersDTO().setId(userId));
        dto1.setBook(bookDTO1);

        BookRatingDTO dto2 = new BookRatingDTO();
        dto2.setId(2);
        dto2.setRating(4);
        dto2.setUser(new UsersDTO().setId(userId));
        dto2.setBook(bookDTO2);

        when(bookRatingRepository.findByUserId(userId)).thenReturn(ratings);
        when(bookRatingMapper.toDTO(argThat(br -> br != null && br.getId() == 1))).thenReturn(dto1);
        when(bookRatingMapper.toDTO(argThat(br -> br != null && br.getId() == 2))).thenReturn(dto2);

        List<BookRatingDTO> result = bookRatingService.getRatingsByUserId(userId);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));

        verify(bookRatingRepository).findByUserId(userId);
        verify(bookRatingMapper, times(2)).toDTO(any(BookRating.class));
    }

    @Test
    void getRatingsByUserId_ShouldReturnEmptyList_WhenNoRatingsExist() {
        Integer userId = 1;

        when(bookRatingRepository.findByUserId(userId)).thenReturn(Arrays.asList());

        List<BookRatingDTO> result = bookRatingService.getRatingsByUserId(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(bookRatingRepository).findByUserId(userId);
        verifyNoMoreInteractions(bookRatingMapper);
    }

    @Test
    void deleteRating_ShouldDeleteRating_WhenRatingExists() throws Exception {
        Integer userId = 1;
        Integer bookId = 101;

        BookRating existingRating = new BookRating()
                .setId(1)
                .setUser(new Users().setId(userId))
                .setBook(new Books().setId(bookId))
                .setRating(5);

        when(bookRatingRepository.findByUserIdAndBookId(userId, bookId))
                .thenReturn(Optional.of(existingRating));

        bookRatingService.deleteRating(userId, bookId);

        verify(bookRatingRepository).delete(existingRating);
    }

    @Test
    void deleteRating_ShouldNotThrowException_WhenRatingDoesNotExist() throws Exception {
        Integer userId = 1;
        Integer bookId = 101;

        when(bookRatingRepository.findByUserIdAndBookId(userId, bookId))
                .thenReturn(Optional.empty());

        bookRatingService.deleteRating(userId, bookId);

        verify(bookRatingRepository, never()).delete(any(BookRating.class));
    }
}
