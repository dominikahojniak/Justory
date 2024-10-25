package com.justory.backend.service;

import com.justory.backend.api.external.BooksDTO;
import com.justory.backend.api.internal.Books;
import com.justory.backend.api.internal.UserToReadList;
import com.justory.backend.api.internal.Users;
import com.justory.backend.mapper.BooksMapper;
import com.justory.backend.repository.BooksRepository;
import com.justory.backend.repository.UserToReadListRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserToReadListServiceImpl implements UserToReadListService {

    private final UserToReadListRepository userToReadListRepository;
    private final BooksMapper booksMapper;

    @Autowired
    public UserToReadListServiceImpl(UserToReadListRepository userToReadListRepository, BooksMapper booksMapper) {
        this.userToReadListRepository = userToReadListRepository;
        this.booksMapper = booksMapper;
    }
    @Override
    public List<BooksDTO> getUserToReadBooks(Integer userId) {
        List<UserToReadList> userToReadList = userToReadListRepository.findByUserId(userId);
        List<BooksDTO> toReadBooks = new ArrayList<>();
        for (UserToReadList entry : userToReadList) {
            toReadBooks.add(booksMapper.toDTO(entry.getBook()));
        }
        return toReadBooks;
    }
    public void addBookToUserToReadList(Integer userId, Integer bookId) throws Exception {
        Optional<UserToReadList> existingEntry = userToReadListRepository.findByUserIdAndBookId(userId, bookId);
        if (existingEntry.isPresent()) {
            throw new Exception("Book is already in the user's to-read list");
        }

        Users user = new Users();
        user.setId(userId);

        Books book = new Books();
        book.setId(bookId);

        UserToReadList userToReadList = new UserToReadList();
        userToReadList.setUser(user);
        userToReadList.setBook(book);
        userToReadList.setDateAdded(LocalDate.now());
        userToReadListRepository.save(userToReadList);
    }

    @Transactional
    @Override
    public void removeBookFromUserToReadList(Integer userId, Integer bookId) {
        Optional<UserToReadList> entry = userToReadListRepository.findByUserIdAndBookId(userId, bookId);
        if (entry.isPresent()) {
            userToReadListRepository.deleteByUserIdAndBookId(userId, bookId);
            System.out.println("Successfully removed book with ID " + bookId + " from user's ToRead list");
        } else {
            System.out.println("Book with ID " + bookId + " not found in user's ToRead list");
        }
    }

}