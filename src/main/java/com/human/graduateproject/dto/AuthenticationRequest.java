package com.human.graduateproject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class AuthenticationRequest {

    private String username;
    private String password;
}
