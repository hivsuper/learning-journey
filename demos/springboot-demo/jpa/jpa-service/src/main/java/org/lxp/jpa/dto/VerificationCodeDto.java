package org.lxp.jpa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationCodeDto {
    private String key;
    private String image;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String text;
}
