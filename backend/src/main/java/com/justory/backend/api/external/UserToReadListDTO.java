package com.justory.backend.api.external;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class UserToReadListDTO {
    private Integer id;
    private UsersDTO user;
    private BooksDTO book;
    private LocalDate dateAdded;
}