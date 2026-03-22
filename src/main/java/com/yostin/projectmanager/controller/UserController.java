package com.yostin.projectmanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yostin.projectmanager.dto.DeleteAccountDTO;
import com.yostin.projectmanager.dto.UserCreateDTO;
import com.yostin.projectmanager.dto.UserResponseDTO;
import com.yostin.projectmanager.dto.UserUpdateDTO;
import com.yostin.projectmanager.model.User;
import com.yostin.projectmanager.service.UserService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@RestController
@RequestMapping("api/users")

public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestBody UserCreateDTO dto) {
        return userService.createUser(dto);
    }

    @GetMapping
    public List<UserResponseDTO> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    
    @PutMapping("/{id}")
    public User modUser(@PathVariable Long id, @RequestBody UserUpdateDTO dto){
        return userService.updateUser(id, dto.getName());
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyAccount(@RequestBody DeleteAccountDTO dto, Authentication authentication) {
        String email = authentication.getName();
        userService.deleteByEmailAndPassword(email, dto.getPassword());

        return ResponseEntity.noContent().build();
    }

}
