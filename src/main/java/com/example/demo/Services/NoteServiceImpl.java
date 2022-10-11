package com.example.demo.Services;

import com.example.demo.Entities.Note;
import com.example.demo.Repositories.Store;
import com.example.demo.Requests.POST.NoteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Date;
import java.sql.Timestamp;
import java.util.Objects;

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
            System.out.println("HttpStatus.UNAUTHORIZED");
            return new ResponseEntity<>(
                    HttpStatus.UNAUTHORIZED
            );
        }
        System.out.println("HttpStatus.OK");
        return ResponseEntity.status(HttpStatus.OK)
                .body(store.getNotesJson(username));
    }

    @Override
    public ResponseEntity<String> addNote(@RequestHeader String username, @RequestBody NoteRequest noteRequest) {
        if (unauthorized(username)) {
            System.out.println("HttpStatus.UNAUTHORIZED");
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
        System.out.println("HttpStatus.CREATED");

        return new ResponseEntity<>(
                HttpStatus.CREATED
        );
    }

    @Override
    public ResponseEntity<String> editNote(@PathVariable Long id, @RequestHeader String username, @RequestBody NoteRequest noteRequest) {
        if (unauthorized(username)) {
            System.out.println("HttpStatus.UNAUTHORIZED");
            return new ResponseEntity<>(
                    HttpStatus.UNAUTHORIZED
            );
        }

        if (store.findNoteById(id, username) == null) {
            System.out.println("HttpStatus.NOT_FOUND");
            return new ResponseEntity<>(
                    HttpStatus.NOT_FOUND
            );
        }

        Note note = new Note();

        note.setContent(noteRequest.getContent());
        note.setTitle(noteRequest.getTitle());
        note.setUser(store.findUserByUsername(username));
        note.setId(id);
        note.setDate(store.findNoteById(id, username).getDate());

        store.editNote(note, username);

        System.out.println("HttpStatus.CREATED");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(note.toString());
    }

    @Override
    public ResponseEntity<String> deleteNote(@PathVariable Long id, @RequestHeader String username) {
        if (unauthorized(username)) {
            System.out.println("HttpStatus.UNAUTHORISED");
            return new ResponseEntity<>(
                    HttpStatus.UNAUTHORIZED
            );
        }

        if (store.findNoteById(id, username) == null) {
            System.out.println("HttpStatus.NOT_FOUND");
            return new ResponseEntity<>(
                    HttpStatus.NOT_FOUND
            );
        }

        store.deleteNote(id, username);

        System.out.println("HttpStatus.NO_CONTENT");
        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT
        );
    }

}
