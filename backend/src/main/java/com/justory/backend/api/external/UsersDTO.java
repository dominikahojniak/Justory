package com.justory.backend.api.external;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class UsersDTO {
    private Integer id;
    private String email;
    private String name;
    private UsersFeaturesDTO userFeaturesID;
    private String role;
}