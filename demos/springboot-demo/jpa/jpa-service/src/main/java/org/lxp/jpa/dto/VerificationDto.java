package org.lxp.jpa.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerificationDto {
    @NotBlank
    private String verifyCodeKey;

    @NotBlank
    private String verifyCode;
}
