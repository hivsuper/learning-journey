package org.lxp.gradle.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestTableDto {
    @NotNull
    private Integer id;

    @NotEmpty
    private String name;
}
