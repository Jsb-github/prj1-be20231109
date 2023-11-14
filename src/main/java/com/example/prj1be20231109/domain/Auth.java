package com.example.prj1be20231109.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auth {

    private Integer id;

    private String memberId;

    private String name;
}
