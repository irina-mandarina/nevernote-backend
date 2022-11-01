package com.example.demo.Services;

import com.example.demo.Entities.Note;
import com.example.demo.Entities.User;
import com.example.demo.Repositories.NoteRepository;
import com.example.demo.Requests.GET.GetNote;
import com.example.demo.Requests.GET.GetNotes;
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
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;

    private final UserService userService;
    private final LoggedService loggedService;

    boolean unauthorized(String username) {
        return username.isEmpty() || (userService.findByUsername(username) == null) || !loggedService.isLogged(userService.findByUsername(username));
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
                .body(gson.toJson( new GetNotes( findAllByUser(userService.findByUsername(username)) )));
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
        note.setUser(userService.findByUsername(username));

        Date date = new Date();
        note.setDate(new Timestamp(date.getTime()));

        noteRepository.save(note);

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

        Note note = findNoteById(id);

        if (note == null || !Objects.equals(note.getUser().getUsername(), username)) {
            return new ResponseEntity<>(
                    HttpStatus.NOT_FOUND
            );
        }

        note.setContent(noteRequest.getContent());
        note.setTitle(noteRequest.getTitle());

        noteRepository.save(note);

        Gson gson = new Gson();

        final HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);


        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(httpHeaders)
                .body(gson.toJson( new GetNote(note) ));
    }

    @Override
    public ResponseEntity<String> deleteNote(@PathVariable Long id, @RequestHeader String username) {
        if (unauthorized(username)) {
            return new ResponseEntity<>(
                    HttpStatus.UNAUTHORIZED
            );
        }

        Note note = noteRepository.findNoteById(id);

        if (note == null || !Objects.equals(note.getUser().getUsername(), username)) {
            return new ResponseEntity<>(
                    HttpStatus.NOT_FOUND
            );
        }

        noteRepository.delete(note);

        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT
        );
    }

    private List<Note> findAllByUser(User user) {
        return noteRepository.findAllByUser(user);
    }

    private Note findNoteById(Long id) {
        return noteRepository.findNoteById(id);
    }
}
