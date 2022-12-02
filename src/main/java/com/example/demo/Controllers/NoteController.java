package com.example.demo.Controllers;

import com.example.demo.Requests.POST.NoteRequest;
import com.example.demo.Services.NoteService;
import com.example.demo.types.NoteType;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:3000")
@AllArgsConstructor
public class NoteController {
    NoteService noteService;

    @GetMapping("/notes")
    ResponseEntity<String> getNotes(@RequestHeader String username, @RequestParam NoteType noteType) {
        return noteService.getNotes(username, noteType);
    }

    @PostMapping("/notes")
    ResponseEntity<String> addNote(@RequestHeader String username, @RequestBody NoteRequest noteRequest) {
        return noteService.addNote(username, noteRequest);
    }

    @GetMapping("/notes/{id}")
    ResponseEntity<String> getNotes(@RequestHeader String username, @PathVariable("id") Long id) {
        return noteService.getNote(username, id);
    }

    @PutMapping("/notes/{id}")
    ResponseEntity<String> editNote(@PathVariable("id") Long id, @RequestHeader String username, @RequestBody NoteRequest noteRequest) {
        return noteService.editNote(id, username, noteRequest);
    }

    @DeleteMapping("/notes/{id}")
    ResponseEntity<String> deleteNote(@PathVariable("id") Long id, @RequestHeader String username) {
        return noteService.deleteNote(id, username);
    }

    @PutMapping("/tasks/{id}")
    ResponseEntity<String> completeTask(@PathVariable("id") Long id, @RequestHeader String username) {
        return noteService.completeTask(id, username);
    }
}
