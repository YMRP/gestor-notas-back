package com.yostin.projectmanager.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.yostin.projectmanager.dto.NoteCreateDTO;
import com.yostin.projectmanager.dto.NoteResponseDTO;
import com.yostin.projectmanager.model.Note;
import com.yostin.projectmanager.model.User;
import com.yostin.projectmanager.repository.NoteRespository;
import com.yostin.projectmanager.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NoteService {
        private NoteRespository noteRespository;
        private UserRepository userRepository;

        public NoteResponseDTO create(NoteCreateDTO dto) {
                String email = SecurityContextHolder.getContext().getAuthentication().getName();

                User user = userRepository.findByEmail(email).orElseThrow();

                Note note = Note.builder().title(dto.getTitle()).content(dto.getContent())
                                .createdAt(LocalDateTime.now())
                                .user(user).build();

                Note saved = noteRespository.save(note);

                return new NoteResponseDTO(
                                saved.getId(),
                                saved.getTitle(),
                                saved.getContent(),
                                saved.getCreatedAt());
        }

        public List<NoteResponseDTO> getNotes() {

                String email = SecurityContextHolder.getContext()
                                .getAuthentication()
                                .getName();

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("No se encuentra el usuario"));

                List<Note> notes = noteRespository.findByUser(user);

                return notes.stream()
                                .map(note -> new NoteResponseDTO(
                                                note.getId(),
                                                note.getTitle(),
                                                note.getContent(), note.getCreatedAt()))
                                .toList();
        }

        public NoteResponseDTO update(Long noteId, NoteCreateDTO dto) {
                String email = SecurityContextHolder.getContext().getAuthentication().getName();

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new IllegalArgumentException("No se encuentra el usuario"));

                Note note = noteRespository.findById(noteId).orElseThrow();

                // SI EL MONO ES DISTINTO AL MONO QUE EDITO ESTA VAINA, ENTONCES NO TE DEJA, NO
                // PUEDES EDITAR NADA SI TU NO ERES EL PROPIETARIO
                if (!note.getUser().getId().equals(user.getId())) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
                }

                note.setTitle(dto.getTitle());

                note.setContent(dto.getContent());
                note.setCreatedAt(LocalDateTime.now());

                Note updated = noteRespository.save(note);

                return new NoteResponseDTO(updated.getId(), updated.getTitle(), updated.getContent(),
                                updated.getCreatedAt());
        }

        public void deleteNote(Long noteId) {
                String email = SecurityContextHolder.getContext().getAuthentication().getName();

                User user = userRepository.findByEmail(email).orElseThrow();

                Note note = noteRespository.findById(noteId).orElseThrow();

                if (!note.getUser().getId().equals(user.getId())) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
                }

                noteRespository.delete(note);
        }

}
