package com.justory.backend.api.internal;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Entity
@Table(name = "publishers")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Publishers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String address;
    private String website;

    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL)
    private Set<Books> books;
}