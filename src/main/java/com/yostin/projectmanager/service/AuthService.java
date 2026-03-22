package com.yostin.projectmanager.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.yostin.projectmanager.dto.LoginRequestDTO;
import com.yostin.projectmanager.dto.LoginResponseDTO;
import com.yostin.projectmanager.model.RefreshToken;
import com.yostin.projectmanager.model.User;
import com.yostin.projectmanager.repository.UserRepository;
import com.yostin.projectmanager.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AuthService {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private JwtService jwtService;
    private RefreshTokenService refreshTokenService;

    public LoginResponseDTO login(LoginRequestDTO dto) {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Credenciales incorrectas"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
        }

        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail());

        return new LoginResponseDTO(accessToken, refreshToken.getToken());
    }

    public LoginResponseDTO refresh(String refreshToken) {

        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(tokenEntity -> {
                    User user = tokenEntity.getUser();
                    String newAccessToken = jwtService.generateToken(user);

                    return new LoginResponseDTO(newAccessToken, refreshToken);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token inválido"));
    }

}
