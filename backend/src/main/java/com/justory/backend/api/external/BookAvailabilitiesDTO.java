package com.justory.backend.api.external;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class BookAvailabilitiesDTO {
    private Integer bookId;
    private String platformName;
    private byte[] platformLogo;
    private String formatName;
    private byte[] formatLogo;
    private String accessTypeName;
}
