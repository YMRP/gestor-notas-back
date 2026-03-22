package com.yostin.projectmanager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yostin.projectmanager.dto.NoteCreateDTO;
import com.yostin.projectmanager.dto.NoteResponseDTO;
import com.yostin.projectmanager.service.NoteService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private NoteService noteService;

    @PostMapping
    public NoteResponseDTO create(@RequestBody NoteCreateDTO dto) {
        return noteService.create(dto);
    }

    @GetMapping
    public List<NoteResponseDTO> getNotes() {
        return noteService.getNotes();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        noteService.deleteNote(id);
    }

    @PutMapping("/{id}")
    public NoteResponseDTO udpate(@PathVariable Long id, @RequestBody NoteCreateDTO dto){
        return noteService.update(id, dto);
    }
}   //PENDIENTE DE PROBAR ESTOS ULTIMOS DOS ENPOINTS
