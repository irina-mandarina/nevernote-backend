package com.example.demo.Controllers;

import com.example.demo.services.LogService;
import com.example.demo.models.POST.NoteRequest;
import com.example.demo.services.NoteService;
import com.example.demo.types.Method;
import com.example.demo.types.NoteType;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:3000")
@AllArgsConstructor
public class NoteController {
    NoteService noteService;
    LogService logService;

    @GetMapping("/notes")
    ResponseEntity<String> getNotes(@RequestAttribute String username, @RequestParam NoteType noteType) {
        ResponseEntity<String> response = noteService.getNotes(username, noteType);
        logService.log(response, username, Method.GET, "/notes" + "?" + noteType.name());
        return response;
    }

    @PostMapping("/notes")
    ResponseEntity<String> addNote(@RequestAttribute String username, @RequestBody NoteRequest noteRequest) {
        ResponseEntity<String> response = noteService.addNote(username, noteRequest);
        logService.log(response, username, Method.POST, "/notes");
        return response;
    }

    @GetMapping("/notes/{id}")
    ResponseEntity<String> getNotes(@RequestAttribute String username, @PathVariable("id") Long id) {
        ResponseEntity<String> response = noteService.getNote(username, id);
        logService.log(response, username, Method.GET, "/notes/" + id);
        return response;
    }

    @PutMapping("/notes/{id}")
    ResponseEntity<String> editNote(@PathVariable("id") Long id, @RequestAttribute String username, @RequestBody NoteRequest noteRequest) {
        ResponseEntity<String> response = noteService.editNote(id, username, noteRequest);
        logService.log(response, username, Method.PUT, "/notes/" + id);
        return response;
    }

    @DeleteMapping("/notes/{id}")
    ResponseEntity<String> deleteNote(@PathVariable("id") Long id, @RequestAttribute String username) {
        ResponseEntity<String> response = noteService.deleteNote(id, username);
        logService.log(response, username, Method.DELETE, "/notes/" + id);
        return response;
    }

    @PutMapping("/notes/{id}/completed")
    ResponseEntity<String> completeTask(@PathVariable("id") Long id, @RequestAttribute String username) {
        ResponseEntity<String> response = noteService.completeTask(id, username);
        logService.log(response, username, Method.PUT, "/notes/" + id + "/completed");
        return response;
    }

    @PutMapping("/notes/{id}/privacy")
    ResponseEntity<String> togglePrivacy(@PathVariable("id") Long id, @RequestAttribute String username) {
        ResponseEntity<String> response = noteService.togglePrivacy(id, username);
        logService.log(response, username, Method.PUT, "/notes/" + id + "/privacy");
        return response;
    }
}
