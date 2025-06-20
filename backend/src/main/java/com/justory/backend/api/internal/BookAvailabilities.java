package com.justory.backend.api.internal;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Objects;

@Data
@Entity
@Table(name = "book_availabilities")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class BookAvailabilities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Books book;

    @ManyToOne
    @JoinColumn(name = "platform_id")
    private Platforms platform;

    @ManyToOne
    @JoinColumn(name = "format_id")
    private Formats format;

    @ManyToOne
    @JoinColumn(name = "access_type_id")
    private AccessTypes accessType;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookAvailabilities)) return false;
        BookAvailabilities that = (BookAvailabilities) o;
        return Objects.equals(book.getId(), that.book.getId()) &&
                Objects.equals(platform.getId(), that.platform.getId()) &&
                Objects.equals(format.getId(), that.format.getId()) &&
                Objects.equals(accessType.getId(), that.accessType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(book.getId(), platform.getId(), format.getId(), accessType.getId());
    }
}