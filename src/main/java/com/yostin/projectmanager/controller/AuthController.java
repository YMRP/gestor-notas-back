package com.yostin.projectmanager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yostin.projectmanager.dto.LoginRequestDTO;
import com.yostin.projectmanager.dto.LoginResponseDTO;
import com.yostin.projectmanager.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/auth")
@AllArgsConstructor
public class AuthController {
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO dto,
            HttpServletResponse response) {

        LoginResponseDTO loginResponse = authService.login(dto);

        Cookie refreshCookie = new Cookie("refreshToken", loginResponse.getRefreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false); // true en producción
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(refreshCookie);

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh")
    public LoginResponseDTO refreshToken(@CookieValue("refreshToken") String refreshToken) {
        return authService.refresh(refreshToken);
    }
}
