package Services;

import Requests.POST.NoteRequest;
import org.springframework.http.ResponseEntity;

public interface NoteService {

    ResponseEntity<String> getNotes(String username);

    ResponseEntity<String> addNote(String username, NoteRequest noteRequest);

    ResponseEntity<String> editNote(Long id, String username, NoteRequest noteRequest);

    ResponseEntity<String> deleteNote(Long id, String username);
}
