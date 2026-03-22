package com.yostin.projectmanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.yostin.projectmanager.model.User;
public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
