package com.example.demo.Services;

import com.example.demo.Entities.Note;
import com.example.demo.Entities.User;
import com.example.demo.Repositories.NoteRepository;
import com.example.demo.Repositories.PermissionRepository;
import com.example.demo.models.GET.NoteResponse;
import com.example.demo.models.GET.GetNotes;
import com.example.demo.models.POST.NoteRequest;
import com.example.demo.types.Method;
import com.example.demo.types.NoteType;
import com.example.demo.types.Privacy;
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
    private final PermissionRepository permissionRepository;
    private final UserService userService;
    private final LoggedService loggedService;

    boolean unauthorized(String username) {
        return username.isEmpty() || (userService.findByUsername(username) == null) || !loggedService.isLogged(userService.findByUsername(username));
    }
    @Override
    public ResponseEntity<String> getNotes(@RequestHeader String username, NoteType noteType) {
        User user = userService.findByUsername(username);
        Timestamp now = new Timestamp((new Date()).getTime());
        List<Note> notes;

        switch (noteType) {
            case TODO -> notes = findAllByUserAndCompletedFalseAndDeadlineAfter(user, now);
            case NOTES -> notes = findAllByUserAndDeadlineIsNull(user);
            case TASKS -> notes = findAllByUserAndDeadlineIsNotNull(user);
            case COMPLETED -> notes = findAllByUserAndDeadlineIsNotNullAndCompletedTrue(user);
            default -> notes = findAllByUser(user);
        }

        final HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        Gson gson = new Gson();

        return ResponseEntity.status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(gson.toJson( new GetNotes( notes )));
    }

    @Override
    public ResponseEntity<String> addNote(@RequestHeader String username, @RequestBody NoteRequest noteRequest) {
        Note note = new Note();
        note.setContent(noteRequest.getContent());
        note.setTitle(noteRequest.getTitle());
        note.setUser(userService.findByUsername(username));
        if (!Objects.isNull(noteRequest.getDeadline())) {
            note.setDeadline(noteRequest.getDeadline());
        }
        note.setPrivacy(noteRequest.getPrivacy());

        Date date = new Date();
        note.setDate(new Timestamp(date.getTime()));

        Gson gson = new Gson();
        final HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(httpHeaders)
                .body(gson.toJson( new NoteResponse(noteRepository.save(note)) ));
    }

    @Override
    public ResponseEntity<String> editNote(@PathVariable Long id, @RequestHeader String username, @RequestBody NoteRequest noteRequest) {

        Note note = findNoteById(id);

        if (note == null || !Objects.equals(note.getUser().getUsername(), username)) {
            return new ResponseEntity<>(
                    HttpStatus.NOT_FOUND
            );
        }

        note.setContent(noteRequest.getContent());
        note.setTitle(noteRequest.getTitle());
        if (!Objects.isNull(noteRequest.getDeadline())) {
            note.setDeadline(noteRequest.getDeadline());
        }

        noteRepository.save(note);

        Gson gson = new Gson();

        final HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);


        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(httpHeaders)
                .body(gson.toJson( new NoteResponse(note) ));
    }

    @Override
    public ResponseEntity<String> deleteNote(@PathVariable Long id, @RequestHeader String username) {
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

    @Override
    public ResponseEntity<String> completeTask(Long id, String username) {
        Note note = noteRepository.findNoteById(id);

        if (note == null || !Objects.equals(note.getUser().getUsername(), username)) {
            return new ResponseEntity<>(
                    HttpStatus.NOT_FOUND
            );
        }

        note.setCompleted(!note.getCompleted());
        noteRepository.save(note);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        (new Gson()).toJson(new NoteResponse(note))
                );
    }

    @Override
    public ResponseEntity<String> getNote(String username, Long id) {
        Note note = noteRepository.findNoteById(id);

        if (note == null) {
            return new ResponseEntity<>(
                    HttpStatus.NOT_FOUND
            );
        }

        // if the note is private
        if (note.getPrivacy().equals(Privacy.PRIVATE)) {
            // if the requester is not the owner of the note
            // ,and he does not have a read permission
            if (!Objects.equals(note.getUser().getUsername(), username) &&
                   Objects.isNull( permissionRepository.findPermissionByNoteIdAndUserUsernameAndPermissionType(
                           note.getId(), username, Method.GET
                   ) )) {
                return new ResponseEntity<>(
                        HttpStatus.FORBIDDEN
                );
            }
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        (new Gson()).toJson(new NoteResponse(note))
                );
    }

    @Override
    public ResponseEntity<String> togglePrivacy(Long id, String username) {
        Note note = noteRepository.findNoteById(id);

        if (note == null) {
            return new ResponseEntity<>(
                    HttpStatus.NOT_FOUND
            );
        }

        if (!Objects.equals(note.getUser().getUsername(), username) && note.getPrivacy().equals(Privacy.PRIVATE)) {
            return new ResponseEntity<>(
                    HttpStatus.FORBIDDEN
            );
        }

        if (note.getPrivacy() == Privacy.PRIVATE) {
            note.setPrivacy(Privacy.PUBLIC);
        }
        else {
            note.setPrivacy(Privacy.PRIVATE);
        }
        noteRepository.save(note);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        (new Gson()).toJson(new NoteResponse(note))
                );
    }

    @Override
    public List<Note> findAllByUser(User user) {
        return noteRepository.findAllByUser(user);
    }

    @Override
    public List<Note> findAllByUserAndDeadlineIsNull(User user) {
        return noteRepository.findAllByUserAndDeadlineIsNull(user);
    }
    @Override
    public List<Note> findAllByUserAndDeadlineIsNotNull(User user) {
        return noteRepository.findAllByUserAndDeadlineIsNotNull(user);
    }
    @Override
    public List<Note> findAllByUserAndCompletedFalseAndDeadlineAfter(User user, Timestamp now) {
        return noteRepository.findAllByUserAndCompletedFalseAndDeadlineAfter(user, now);
    }

    @Override
    public List<Note> findAllByUserAndDeadlineIsNotNullAndCompletedTrue(User user) {
        return noteRepository.findAllByUserAndDeadlineIsNotNullAndCompletedTrue(user);
    }

    @Override
    public Note findNoteById(Long id) {
        return noteRepository.findNoteById(id);
    }
}
