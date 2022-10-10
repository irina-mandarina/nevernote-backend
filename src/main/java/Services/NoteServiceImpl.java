package Services;

import Entities.Note;
import Repositories.Store;
import Requests.POST.NoteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.sql.Timestamp;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    Store store;

    boolean unauthorized(String username) {
        return !Objects.equals(username, store.getLoggedUser().getUsername()) || username.isEmpty();
    }
    @Override
    public ResponseEntity<String> getNotes(String username) {
        if (unauthorized(username)) {
            return new ResponseEntity<>(
                    HttpStatus.UNAUTHORIZED
            );
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(store.getNotesStr(username));
    }

    @Override
    public ResponseEntity<String> addNote(String username, NoteRequest noteRequest) {
        if (unauthorized(username)) {
            return new ResponseEntity<>(
                    HttpStatus.UNAUTHORIZED
            );
        }
        Note note = new Note();
        note.setContent(noteRequest.getContent());
        note.setTitle(noteRequest.getTitle());
        note.setUser(store.getLoggedUser());
        note.setId((long) store.getNotes().get(store.getLoggedUser()).size());

        Date date = new Date();
        note.setDate(new Timestamp(date.getTime()));

        store.addNote(note);

        return new ResponseEntity<>(
                HttpStatus.CREATED
        );
    }

    @Override
    public ResponseEntity<String> editNote(Long id, String username, NoteRequest noteRequest) {
        if (unauthorized(username)) {
            return new ResponseEntity<>(
                    HttpStatus.UNAUTHORIZED
            );
        }

        if (store.findNoteById(id) == null) {
            return new ResponseEntity<>(
                    HttpStatus.NOT_FOUND
            );
        }

        Note note = new Note();

        note.setContent(noteRequest.getContent());
        note.setTitle(noteRequest.getTitle());
        note.setUser(store.getLoggedUser());
        note.setId(id);
        note.setDate(store.findNoteById(id).getDate());

        store.editNote(note);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(note.toString());
    }

    @Override
    public ResponseEntity<String> deleteNote(Long id, String username) {
        if (unauthorized(username)) {
            return new ResponseEntity<>(
                    HttpStatus.UNAUTHORIZED
            );
        }

        if (store.findNoteById(id) == null) {
            return new ResponseEntity<>(
                    HttpStatus.NOT_FOUND
            );
        }

        store.deleteNote(id);

        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT
        );
    }


}
