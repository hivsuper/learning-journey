package org.lxp.gradle.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TestTableDto {
    @NotNull
    private Integer id;

    @NotEmpty
    private String name;
}
