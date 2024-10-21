package com.justory.backend.api.external;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class PublishersDTO {
    private Integer id;
    private String name;
    private String address;
    private String website;
}