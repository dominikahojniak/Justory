package com.justory.backend.mapper;


import com.justory.backend.api.external.BooksDTO;
import com.justory.backend.api.internal.Books;
import com.justory.backend.service.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class BooksMapper {
    @Autowired
    private AuthorsMapper authorsMapper;

    @Autowired
    private CategoriesMapper categoriesMapper;

    @Autowired
    private PublishersMapper publishersMapper;

    public BooksDTO toDTO(Books book) {
        return new BooksDTO()
                .setId(book.getId())
                .setTitle(book.getTitle())
                .setLanguage(book.getLanguage())
                .setDescription(book.getDescription())
                .setISBN(book.getISBN())
                .setDate(book.getDate())
                .setImg(FileUtils.readFileFromLocation(book.getImg()))
                .setCategories(categoriesMapper.mapCategoriesToDTO(book.getCategories()))
                .setAuthors(authorsMapper.mapAuthorsToDTO(book.getAuthors()))
                .setPublisher(publishersMapper.toDTO(book.getPublisher()));
    }

    public Books toEntity(BooksDTO bookDTO) {
        String imgPath = "./uploads/books/" + bookDTO.getTitle() + ".jpg";
        return new Books()
                .setTitle(bookDTO.getTitle())
                .setLanguage(bookDTO.getLanguage())
                .setDescription(bookDTO.getDescription())
                .setISBN(bookDTO.getISBN())
                .setDate(bookDTO.getDate())
                .setImg(imgPath)
                .setAuthors(authorsMapper.mapAuthorsToEntity(bookDTO.getAuthors()))
                .setCategories(categoriesMapper.mapCategoriesToEntity(bookDTO.getCategories()))
                .setPublisher(publishersMapper.toEntity(bookDTO.getPublisher()));

    }

}