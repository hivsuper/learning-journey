package org.lxp.springboot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    private Integer id;

    private String name;

    private String email;

    private Date createdDate;
}