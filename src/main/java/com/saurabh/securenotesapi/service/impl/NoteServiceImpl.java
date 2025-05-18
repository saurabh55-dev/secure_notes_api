package com.saurabh.securenotesapi.service.impl;

import com.saurabh.securenotesapi.model.Note;
import com.saurabh.securenotesapi.repository.NoteRepository;
import com.saurabh.securenotesapi.service.NoteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public Note createNoteForUser(String username, String content) {
        Note note = new Note();
        note.setContent(content);
        note.setOwnerUsername(username);
        return noteRepository.save(note);
    }

    @Override
    public Note updateNoteForUser(Long noteId, String content, String username) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        note.setContent(content);
        return noteRepository.save(note);
    }

    @Override
    public void deleteNoteForUser(Long noteId, String username) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        noteRepository.delete(note);
    }

    @Override
    public List<Note> getNotesForUser(String username) {
        return noteRepository.findByOwnerUsername(username);
    }
}
