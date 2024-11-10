package com.justory.backend.api.external;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class BookRatingDTO {
    private Integer id;
    private UsersDTO user;
    private BooksDTO book;
    private int rating;
}
