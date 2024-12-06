package com.justory.backend;

import com.justory.backend.api.external.AuthorsDTO;
import com.justory.backend.api.external.BookRatingDTO;
import com.justory.backend.api.external.BooksDTO;
import com.justory.backend.api.external.CategoriesDTO;
import com.justory.backend.api.internal.Authors;
import com.justory.backend.api.internal.Books;
import com.justory.backend.api.internal.Categories;
import com.justory.backend.mapper.BooksMapper;
import com.justory.backend.repository.BooksRepository;
import com.justory.backend.service.BookRatingService;
import com.justory.backend.service.RecommendationServiceImpl;
import com.justory.backend.service.UserToReadListService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecommendationServiceTest {
    @Mock
    private UserToReadListService userToReadListService;

    @Mock
    private BookRatingService bookRatingService;

    @Mock
    private BooksRepository booksRepository;

    @Mock
    private BooksMapper booksMapper;

    @InjectMocks
    private RecommendationServiceImpl recommendationService;

    @Test
    void getRecommendationsForUser_ShouldReturnEmptyList_WhenUserHasNoData() {
        Integer userId = 1;

        when(userToReadListService.getUserToReadBooks(userId)).thenReturn(Collections.emptyList());
        when(bookRatingService.getRatingsByUserId(userId)).thenReturn(Collections.emptyList());

        List<BooksDTO> result = recommendationService.getRecommendationsForUser(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Result should be empty when user has no data");

        verify(userToReadListService).getUserToReadBooks(userId);
        verify(bookRatingService).getRatingsByUserId(userId);
        verifyNoMoreInteractions(userToReadListService, bookRatingService, booksRepository, booksMapper);
    }

    @Test
    void getRecommendationsForUser_ShouldReturnRecommendations_WhenUserHasRatedBooks() {
        Integer userId = 1;

        BooksDTO ratedBookDTO = new BooksDTO();
        ratedBookDTO.setId(1);
        ratedBookDTO.setTitle("Rated Book");
        ratedBookDTO.setCategories((Set<CategoriesDTO>) Set.of(new CategoriesDTO().setName("Fiction").setId(1)));
        ratedBookDTO.setAuthors((Set<AuthorsDTO>) Set.of(new AuthorsDTO().setId(1).setFirstName("Tom").setLastName("Riddle")));

        BookRatingDTO bookRatingDTO = new BookRatingDTO();
        bookRatingDTO.setRating(5);
        bookRatingDTO.setBook(ratedBookDTO);

        when(userToReadListService.getUserToReadBooks(userId)).thenReturn(Collections.emptyList());
        when(bookRatingService.getRatingsByUserId(userId)).thenReturn(List.of(bookRatingDTO));

        Books potentialBook = new Books();
        potentialBook.setId(2);
        potentialBook.setTitle("Potential Book");
        potentialBook.setCategories((Set<Categories>) Set.of(new Categories().setName("Fiction").setId(1)));
        potentialBook.setAuthors((Set<Authors>) Set.of(new Authors().setId(1).setFirstName("Tom").setLastName("Riddle")));

        when(booksRepository.findBooksByCategoriesOrAuthors(anyList(), anyList()))
                .thenReturn(List.of(potentialBook));

        BooksDTO potentialBookDTO = new BooksDTO();
        potentialBookDTO.setId(2);
        potentialBookDTO.setTitle("Potential Book");

        when(booksMapper.toDTO(potentialBook)).thenReturn(potentialBookDTO);

        List<BooksDTO> result = recommendationService.getRecommendationsForUser(userId);

        assertNotNull(result);
        assertEquals(1, result.size(), "Result size should be 1");
        assertEquals(potentialBookDTO, result.get(0), "The recommended book should be 'Potential Book'");

        verify(userToReadListService, times(2)).getUserToReadBooks(userId);
        verify(bookRatingService, times(2)).getRatingsByUserId(userId);
        verify(booksRepository).findBooksByCategoriesOrAuthors(anyList(), anyList());
        verify(booksMapper).toDTO(potentialBook);
    }

    @Test
    void getRecommendationsForUser_ShouldExcludeAlreadyRatedOrToReadBooks() {
        Integer userId = 1;

        BooksDTO toReadBookDTO = new BooksDTO();
        toReadBookDTO.setId(1);
        toReadBookDTO.setTitle("To Read Book");
        toReadBookDTO.setCategories((Set<CategoriesDTO>) Set.of(new CategoriesDTO().setName("Fiction").setId(1)));
        toReadBookDTO.setAuthors((Set<AuthorsDTO>) Set.of(new AuthorsDTO().setId(1).setFirstName("Tom").setLastName("Riddle")));

        BooksDTO ratedBookDTO = new BooksDTO();
        ratedBookDTO.setId(2);
        ratedBookDTO.setTitle("Rated Book");
        ratedBookDTO.setCategories((Set<CategoriesDTO>) Set.of(new CategoriesDTO().setName("Biography").setId(2)));
        ratedBookDTO.setAuthors((Set<AuthorsDTO>) Set.of(new AuthorsDTO().setId(2).setFirstName("Tom").setLastName("Adams")));

        BookRatingDTO bookRatingDTO = new BookRatingDTO();
        bookRatingDTO.setRating(4);
        bookRatingDTO.setBook(ratedBookDTO);

        when(userToReadListService.getUserToReadBooks(userId)).thenReturn(Arrays.asList(toReadBookDTO));
        when(bookRatingService.getRatingsByUserId(userId)).thenReturn(Arrays.asList(bookRatingDTO));

        Books potentialBook1 = new Books();
        potentialBook1.setId(3);
        potentialBook1.setTitle("Potential Book 1");
        potentialBook1.setCategories((Set<Categories>) Set.of(new Categories().setName("Fiction").setId(1)));
        potentialBook1.setAuthors((Set<Authors>) Set.of(new Authors().setId(3).setFirstName("Alice").setLastName("Wonderland")));

        Books potentialBook2 = new Books();
        potentialBook2.setId(2);
        potentialBook2.setTitle("Rated Book");
        ratedBookDTO.setCategories((Set<CategoriesDTO>) Set.of(new CategoriesDTO().setName("Biography").setId(2)));
        ratedBookDTO.setAuthors((Set<AuthorsDTO>) Set.of(new AuthorsDTO().setId(2).setFirstName("Tom").setLastName("Adams")));

        when(booksRepository.findBooksByCategoriesOrAuthors(anyList(), anyList()))
                .thenReturn(Arrays.asList(potentialBook1, potentialBook2));

        BooksDTO potentialBookDTO1 = new BooksDTO();
        potentialBookDTO1.setId(3);
        potentialBookDTO1.setTitle("Potential Book 1");

        when(booksMapper.toDTO(potentialBook1)).thenReturn(potentialBookDTO1);

        List<BooksDTO> result = recommendationService.getRecommendationsForUser(userId);

        assertNotNull(result);
        assertEquals(1, result.size(), "Result size should be 1 - excluding already known books");
        assertEquals(potentialBookDTO1, result.get(0), "The recommended book should be 'Potential Book 1'");

        verify(userToReadListService, times(2)).getUserToReadBooks(userId);
        verify(bookRatingService, times(2)).getRatingsByUserId(userId);
        verify(booksRepository).findBooksByCategoriesOrAuthors(anyList(), anyList());
        verify(booksMapper).toDTO(potentialBook1);
        verify(booksMapper, never()).toDTO(potentialBook2);
    }

    @Test
    void getRecommendationsForUser_ShouldCalculateScoresCorrectly() {
        Integer userId = 1;

        BooksDTO ratedBookDTO1 = new BooksDTO();
        ratedBookDTO1.setId(1);
        ratedBookDTO1.setTitle("Rated Book 1");
        ratedBookDTO1.setCategories((Set<CategoriesDTO>) Set.of(new CategoriesDTO().setName("Fiction").setId(1)));
        ratedBookDTO1.setAuthors((Set<AuthorsDTO>) Set.of(new AuthorsDTO().setId(1).setFirstName("Tom").setLastName("Riddle")));

        BooksDTO ratedBookDTO2 = new BooksDTO();
        ratedBookDTO2.setId(2);
        ratedBookDTO2.setTitle("Rated Book 2");
        ratedBookDTO2.setCategories((Set<CategoriesDTO>) Set.of(new CategoriesDTO().setId(2).setName("Adventure")));
        ratedBookDTO2.setAuthors((Set<AuthorsDTO>) Set.of(new AuthorsDTO().setId(2).setFirstName("Jane").setLastName("Smith")));

        BookRatingDTO bookRatingDTO1 = new BookRatingDTO();
        bookRatingDTO1.setRating(5);
        bookRatingDTO1.setBook(ratedBookDTO1);

        BookRatingDTO bookRatingDTO2 = new BookRatingDTO();
        bookRatingDTO2.setRating(3);
        bookRatingDTO2.setBook(ratedBookDTO2);

        when(userToReadListService.getUserToReadBooks(userId)).thenReturn(Collections.emptyList());
        when(bookRatingService.getRatingsByUserId(userId)).thenReturn(Arrays.asList(bookRatingDTO1, bookRatingDTO2));

        Books potentialBook1 = new Books();
        potentialBook1.setId(3);
        potentialBook1.setTitle("Potential Book 1");
        potentialBook1.setCategories((Set<Categories>) Set.of(new Categories().setName("Fiction").setId(1)));
        potentialBook1.setAuthors((Set<Authors>) Set.of(new Authors().setId(3).setFirstName("Alice").setLastName("Wonderland")));


        Books potentialBook2 = new Books();
        potentialBook2.setId(4);
        potentialBook2.setTitle("Potential Book 2");
        potentialBook2.setCategories((Set<Categories>) Set.of(new Categories().setName("Adventure").setId(2)));
        potentialBook2.setAuthors((Set<Authors>) Set.of(new Authors().setId(4).setFirstName("Alice").setLastName("Great")));

        when(booksRepository.findBooksByCategoriesOrAuthors(anyList(), anyList()))
                .thenReturn(Arrays.asList(potentialBook1, potentialBook2));

        BooksDTO potentialBookDTO1 = new BooksDTO();
        potentialBookDTO1.setId(3);
        potentialBookDTO1.setTitle("Potential Book 1");

        BooksDTO potentialBookDTO2 = new BooksDTO();
        potentialBookDTO2.setId(4);
        potentialBookDTO2.setTitle("Potential Book 2");

        when(booksMapper.toDTO(potentialBook1)).thenReturn(potentialBookDTO1);
        when(booksMapper.toDTO(potentialBook2)).thenReturn(potentialBookDTO2);

        List<BooksDTO> result = recommendationService.getRecommendationsForUser(userId);

        assertNotNull(result);
        assertEquals(2, result.size(), "Result size should be 2");

        assertEquals(potentialBookDTO1, result.get(0), "First recommended book should be 'Potential Book 1'");
        assertEquals(potentialBookDTO2, result.get(1), "Second recommended book should be 'Potential Book 2'");

        verify(userToReadListService, times(2)).getUserToReadBooks(userId);
        verify(bookRatingService, times(2)).getRatingsByUserId(userId);
        verify(booksRepository).findBooksByCategoriesOrAuthors(anyList(), anyList());
        verify(booksMapper).toDTO(potentialBook1);
        verify(booksMapper).toDTO(potentialBook2);
    }
}
