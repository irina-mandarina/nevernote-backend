package Repositories;

import Entities.Note;
import Entities.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

@Getter
public class Store {
    ArrayList <User> users = new ArrayList<>(1000);
    Map <User, ArrayList<Note>> notes;
    User loggedUser = null;

    public User findUserByUsername(String username) {
        for (User user: users) {
            if (Objects.equals(user.getUsername(), username)) {
                return user;
            }
        }
        return null;
    }

    public boolean usernameExists(String username) {
        for (User user: users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void saveUser(User user) {
        users.add(user); // no checking because I won't use the store for long
    }

    public void logOut() {
        loggedUser = null;
    }

    public String getNotesStr(String username) {
        return notes.get(findUserByUsername(username)).toString();
    }

    public void addNote(Note note) {
        notes.get(loggedUser).add(note);
    }

    public Note findNoteById(Long id) {
        ArrayList<Note> currentUserNotes = new ArrayList<> (notes.get(loggedUser));
        for (Note n: currentUserNotes) {
            if (Objects.equals(n.getId(), id)) {
                return n;
            }
        }
        return null;
    }


    public Long noteIndex(Note note) { // the index in the list: might not be equal to the note id
        Note n = findNoteById(note.getId());
        if (n == null) {
            return (long) -1;
        }
        return n.getId();
    }

    public void editNote(Note note) {
        notes.get(loggedUser).set(Math.toIntExact(noteIndex(note)), note);
    }

    public void deleteNote(Long id) {
        notes.get(loggedUser).remove(findNoteById(id));
    }
}
