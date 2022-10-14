package com.example.demo.Services;

import com.example.demo.Entities.Note;
import com.example.demo.Repositories.Store;
import com.example.demo.Requests.POST.NoteRequest;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Date;
import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final Store store;

    boolean unauthorized(String username) {
        if (username.isEmpty()/* || !store.usernameExists(username) || !store.isLogged(username)*/) {
            System.out.println(username + " is empty");
            return true;
        }
        if (!store.usernameExists(username)) {
            System.out.println(username + " does not exist");
            return true;
        }
        if (!store.isLogged(username)) {
            System.out.println(username + " is not logged");
            return true;
        }
        return false;
    }
    @Override
    public ResponseEntity<String> getNotes(@RequestHeader String username) {
        if (unauthorized(username)) {
            return new ResponseEntity<>(
                    HttpStatus.UNAUTHORIZED
            );
        }

        final HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        Gson gson = new Gson();

        return ResponseEntity.status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(gson.toJson(store.getNotes().get(store.findUserByUsername(username))));
    }

    @Override
    public ResponseEntity<String> addNote(@RequestHeader String username, @RequestBody NoteRequest noteRequest) {
        if (unauthorized(username)) {
            return new ResponseEntity<>(
                    HttpStatus.UNAUTHORIZED
            );
        }
        Note note = new Note();
        note.setContent(noteRequest.getContent());
        note.setTitle(noteRequest.getTitle());
        note.setUser(store.findUserByUsername(username));
        note.setId(store.generateNoteIndex(username));

        Date date = new Date();
        note.setDate(new Timestamp(date.getTime()));

        store.addNote(note, username);

        return new ResponseEntity<>(
                HttpStatus.CREATED
        );
    }

    @Override
    public ResponseEntity<String> editNote(@PathVariable Long id, @RequestHeader String username, @RequestBody NoteRequest noteRequest) {
        if (unauthorized(username)) {
            return new ResponseEntity<>(
                    HttpStatus.UNAUTHORIZED
            );
        }

        if (store.findNoteById(id, username) == null) {
            return new ResponseEntity<>(
                    HttpStatus.NOT_FOUND
            );
        }

        store.editNote(id, username, noteRequest.getTitle(), noteRequest.getContent());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(store.getNoteJson(id, username));
    }

    @Override
    public ResponseEntity<String> deleteNote(@PathVariable Long id, @RequestHeader String username) {
        if (unauthorized(username)) {
            return new ResponseEntity<>(
                    HttpStatus.UNAUTHORIZED
            );
        }

        if (store.findNoteById(id, username) == null) {
            return new ResponseEntity<>(
                    HttpStatus.NOT_FOUND
            );
        }

        store.deleteNote(id, username);

        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT
        );
    }

}
