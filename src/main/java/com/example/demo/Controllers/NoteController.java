package com.example.demo.Controllers;

import com.example.demo.Requests.POST.NoteRequest;
import com.example.demo.Services.NoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class NoteController {
    NoteService noteService;

    @GetMapping("/notes")
    ResponseEntity<String> getNotes(@RequestHeader String username) {
        return noteService.getNotes(username);
    }

    @PostMapping("/notes")
    ResponseEntity<String> addNote(@RequestHeader String username, @RequestBody NoteRequest noteRequest) {
        return noteService.addNote(username, noteRequest);
    }

    @PutMapping("/notes/{id}")
    ResponseEntity<String> editNote(@PathVariable("id") Long id, @RequestHeader String username, @RequestBody NoteRequest noteRequest) {
        return noteService.editNote(id, username, noteRequest);
    }

    @DeleteMapping("/notes/{id}")
    ResponseEntity<String> deleteNote(@PathVariable("id") Long id, @RequestHeader String username) {
        return noteService.deleteNote(id, username);
    }
}
