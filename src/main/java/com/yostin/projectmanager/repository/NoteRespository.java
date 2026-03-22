package com.yostin.projectmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yostin.projectmanager.model.Note;
import com.yostin.projectmanager.model.User;

public interface NoteRespository extends JpaRepository<Note, Long> {
    List <Note> findByUser(User user);

    void deleteByUser(User user);
    
}


