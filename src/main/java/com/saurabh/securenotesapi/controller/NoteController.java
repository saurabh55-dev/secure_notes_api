package com.saurabh.securenotesapi.controller;

import com.saurabh.securenotesapi.model.Note;
import com.saurabh.securenotesapi.service.NoteService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public Note createNote(@RequestBody String content,
                           @AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        System.out.println("User Details: " + username);
        return noteService.createNoteForUser(username, content);
    }

    @GetMapping
    public List<Note> getUserNotes(@AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        System.out.println("User Details: " + username);
        return noteService.getNotesForUser(username);
    }

    @PutMapping("/{noteId}")
    public Note updateNote(@PathVariable Long noteId,
                           @RequestBody String content,
                           @AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        return noteService.updateNoteForUser(noteId, content, username);
    }

    @DeleteMapping("/{noteId}")
    public void deleteNote(@PathVariable Long noteId,
                           @AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        noteService.deleteNoteForUser(noteId, username);
    }
}
