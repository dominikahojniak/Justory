package com.justory.backend.api.external;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class FormatsDTO {
    private Integer id;
    private String name;
    private byte[] img;
}
