package com.example.demo.Services;

import com.example.demo.Requests.POST.NoteRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface NoteService {

    ResponseEntity<String> getNotes(@RequestHeader String username);

    ResponseEntity<String> addNote(@RequestHeader String username, @RequestBody NoteRequest noteRequest);

    ResponseEntity<String> editNote(@PathVariable Long id, @RequestHeader String username, @RequestBody NoteRequest noteRequest);

    ResponseEntity<String> deleteNote(@PathVariable Long id, @RequestHeader String username);
}
