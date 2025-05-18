package com.saurabh.securenotesapi.repository;

import com.saurabh.securenotesapi.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByOwnerUsername(String username);
}
