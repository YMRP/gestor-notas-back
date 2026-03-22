package com.yostin.projectmanager.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yostin.projectmanager.dto.UserCreateDTO;
import com.yostin.projectmanager.dto.UserResponseDTO;
import com.yostin.projectmanager.model.User;
import com.yostin.projectmanager.repository.NoteRespository;
import com.yostin.projectmanager.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

        @Autowired
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final NoteRespository noteRespository;

        public User createUser(UserCreateDTO dto) {
                return userRepository.save(
                                User.builder()
                                                .name(dto.getName())
                                                .email(dto.getEmail())
                                                .password(passwordEncoder.encode(dto.getPassword())).build());
        }

        public User updateUser(Long id, String name){
                User user = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException(("El Usuario no existe")));

                user.setName(name);
                return userRepository.save(user);


        }

        @Transactional // Si algo falla, todo se revierte
        public void deleteByEmailAndPassword(String email, String rawPassword) {
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                // Si se borra el usuario para eliminar la cuenta, se eliminan primeor las notas
                // asociadas

                if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
                        throw new RuntimeException("Contraseña incorrecta");
                }

                noteRespository.deleteByUser(user);
                userRepository.delete(user);
        }

        public List<UserResponseDTO> getAllUsers() {
                return userRepository.findAll()
                                .stream()
                                .map(user -> new UserResponseDTO(
                                                user.getId(),
                                                user.getName(),
                                                user.getEmail(),
                                                user.getCreatedAt()))
                                .collect(Collectors.toList());
        }

        public User getUserById(Long id) {
                return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        }

        public void deleteUserById(Long id) {
                userRepository.deleteById(id);

        }
}
