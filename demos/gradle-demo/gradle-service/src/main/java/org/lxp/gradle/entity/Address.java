package org.lxp.gradle.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Address {
    private String postCode;
    private String city;
}
