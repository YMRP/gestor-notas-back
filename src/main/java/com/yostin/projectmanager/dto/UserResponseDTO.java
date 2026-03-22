package com.yostin.projectmanager.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
}
