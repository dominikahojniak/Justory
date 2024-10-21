package com.justory.backend.api.external;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class BooksDTO {
    private Integer id;
    private String title;
    private String language;
    private String description;
    private String ISBN;
    private LocalDate date;
    private byte[] img;
    private Set<AuthorsDTO> authors = new HashSet<>();
    private Set<CategoriesDTO> categories;
    private PublishersDTO publisher;
    private Set<BookAvailabilitiesDTO> availabilities;
}