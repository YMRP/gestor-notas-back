package com.yostin.projectmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class LoginResponseDTO {

    private String token;
    private String refreshToken;
}
