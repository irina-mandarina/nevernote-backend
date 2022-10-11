package com.example.demo.Repositories;

import com.example.demo.Entities.Note;
import com.example.demo.Entities.User;
import lombok.Getter;

import java.util.*;

@Getter
public class Store {
    ArrayList <User> users = new ArrayList<>(1000);
    Map <User, ArrayList<Note>> notes = new HashMap<>();
    ArrayList <String> loggedUsers = new ArrayList<>(20);

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
                System.out.println(username + " exists.");
                return true;
            }
        }
        System.out.println(username + " does not exist in users.");
        return false;
    }

    public boolean isLogged(String username) {
        for (String u: loggedUsers) {
            if (Objects.equals(u, username)) {
                System.out.println(username + " is logged.");
                return true;
            }
        }
        System.out.println(username + " is not logged.");
        return false;
    }

    public void logUser(String newUsername) {
        if (isLogged(newUsername)) {
            System.out.println(newUsername + " is already logged.");
            return;
        }
        System.out.println("Added to loggedUsers:" + newUsername);
        loggedUsers.add(newUsername);
    }

    public void saveUser(User user) {
        System.out.println("Registered " + user.getUsername());
        users.add(user); // no checking because I won't use the store for long
        System.out.println(users);
    }

    public void logOut(String username) {
        System.out.println(username + " logged out.");
        loggedUsers.remove(username);
    }

    public String getNotesStr(String username) {
        return notes.get(findUserByUsername(username)).toString();
    }

    public void addNote(Note note, String username) {
        if (Objects.isNull(notes.get(findUserByUsername(username)))) {
            notes.put(findUserByUsername(username), new ArrayList<>(100));
        }
        notes.get(findUserByUsername(username)).add(note);
    }

    public Long noteIndex(Note note) { // the index in the list: might not be equal to the note id
        Note n = findNoteById(note.getId(), note.getUser().getUsername());
        if (n == null) {
            return (long) -1;
        }
        return n.getId();
    }

    public Note findNoteById(Long id, String username) {
        for (Note n: notes.get(username)) {
            if (n.getId() == id) {
                return n;
            }
        }
        return null;
    }

    public void editNote(Note note, String username) {
        notes.get(findUserByUsername(username)).set(Math.toIntExact(noteIndex(note)), note);
    }

    public void deleteNote(Long id, String username) {
        notes.get(findUserByUsername(username)).remove(findNoteById(id, username));
    }

    public Long generateNoteIndex(String username) {
        if (Objects.isNull(notes) || Objects.isNull(notes.get(findUserByUsername(username)))) {
            return 0L;
        }
        return Long.valueOf(notes.get(findUserByUsername(username)).size());
    }

    public String getNotesJson(String username) {
        String result = "{\n";
        for (Note note: notes.get(findUserByUsername(username))) {
            result += "\"[content\": " + "\"" + note.getContent() + "\"" + "\n";
            result += "\"date\": " + "\"" + note.getDate() + "\"" + "\n";
            result += "\"id\": " + "\"" + note.getId() + "\"" + "\n";
            result += "\"title\": " + "\"" + note.getTitle() + "\"" + "\n";
            result += "\"user\": " + "\"" + username + "\"" + "], \n";
        }
        result += "\n}";
        return result;
    }
}
