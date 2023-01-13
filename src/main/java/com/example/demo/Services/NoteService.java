package com.example.demo.Services;

import com.example.demo.Entities.Note;
import com.example.demo.Entities.User;
import com.example.demo.Repositories.projections.NoteId;
import com.example.demo.models.POST.NoteRequest;
import com.example.demo.types.NoteType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.sql.Timestamp;
import java.util.List;

public interface NoteService {

    ResponseEntity<String> getNotes(@RequestHeader String username, NoteType noteType);
    ResponseEntity<String> addNote(@RequestHeader String username, @RequestBody NoteRequest noteRequest);
    ResponseEntity<String> editNote(@PathVariable Long id, @RequestHeader String username, @RequestBody NoteRequest noteRequest);
    ResponseEntity<String> deleteNote(@PathVariable Long id, @RequestHeader String username);
    ResponseEntity<String> completeTask(Long id, String username);
    ResponseEntity<String> getNote(String username, Long id);

    ResponseEntity<String> togglePrivacy(Long id, String username);

    List<Note> findAllByUser(User user);
    List<Long> findNotesByUser(User user);

    List<Note> findAllByUserAndDeadlineIsNull(User user);

    List<Note> findAllByUserAndDeadlineIsNotNull(User user);

    List<Note> findAllByUserAndCompletedFalseAndDeadlineAfter(User user, Timestamp now);

    List<Note> findAllByUserAndDeadlineIsNotNullAndCompletedTrue(User user);

    Note findNoteById(Long id);

    List<Note> findAll();

    List<Note> findAllByDeadlineIsNull();

    List<Note> findAllByDeadlineIsNotNull();

    List<Note> findAllByCompletedFalseAndDeadlineAfter(Timestamp now);

    List<Note> findAllByDeadlineIsNotNullAndDeadlineBefore(Timestamp now);

    List<Note> findAllByDeadlineIsNotNullAndCompletedTrue();
}
