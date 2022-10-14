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

    public void initNotes(String username) {
        if (Objects.isNull(notes.get(findUserByUsername(username)))) {
            notes.put(findUserByUsername(username), new ArrayList<>(100));
        }
    }

    public void addNote(Note note, String username) {
        initNotes(username);
        notes.get(findUserByUsername(username)).add(note);
        System.out.println(username + " added a new note: " + notes.get(findUserByUsername(username)).get(notes.get(findUserByUsername(username)).size()-1));
    }

    public Long noteIndex(Note note) { // the index in the list: might not be equal to the note id
        initNotes(note.getUser().getUsername());
        Note n = findNoteById(note.getId(), note.getUser().getUsername());
        if (n == null) {
            return (long) -1;
        }
        return n.getId();
    }

    public Note findNoteById(Long id, String username) {
        initNotes(username);
        for (Note n: notes.get(findUserByUsername(username))) {
            if (Objects.equals(n.getId(), id)) {
                return n;
            }
        }
        return null;
    }

    public void editNote(Long id, String username, String title, String content) {
        initNotes(username);
        for (Note n: notes.get(findUserByUsername(username))) {
            if (Objects.equals(n.getId(), id)) {
                n.setTitle(title);
                n.setContent(content);
                return;
            }
        }
    }

    public void deleteNote(Long id, String username) {
        initNotes(username);
        notes.get(findUserByUsername(username)).remove(findNoteById(id, username));
    }

    public Long generateNoteIndex(String username) {
        if (Objects.isNull(notes) || Objects.isNull(notes.get(findUserByUsername(username)))) {
            return 0L;
        }
        return (long) notes.get(findUserByUsername(username)).size();
    }

    public String getNotesJson(String username) {
        StringBuilder result = new StringBuilder("[\n");
        if (Objects.isNull(notes.get(findUserByUsername(username)))) {
            return "[]";
        }
        for (Note note: notes.get(findUserByUsername(username))) {
            result.append(getNoteJson(note.getId(), username));
            if (note != notes.get(findUserByUsername(username)).get(notes.get(findUserByUsername(username)).size()-1)) {
                result.append(", \n");
            }
        }
        result.append("\n]");
        return result.toString();
    }

    public String getNoteJson(Long id, String username) {
        Note note = findNoteById(id, username);
        if (Objects.isNull(note)) {
            return "{}";
        }
        String result = "{\n";
        result += "\"content\": " + "\"" + note.getContent() + "\"" + "\n";
        result += "\"date\": " + "\"" + note.getDate() + "\"" + "\n";
        result += "\"id\": " + "\"" + note.getId() + "\"" + "\n";
        result += "\"title\": " + "\"" + note.getTitle() + "\"" + "\n";
        result += "\"user\": " + "\"" + username + "\"" + "\n}";
        return result;
    }

    public String userDetails(String username) {
        User user = findUserByUsername(username);
        String result = "{\n";
        result += "    \"username\": " + "\"" + username + "\"\n";
        result += "    \"password\": " + "\"" + user.getPassword() + "\"\n";
        result += "    \"name\": " + "\"" + user.getName() + "\"\n";
        result += "    \"age\": " + user.getAge() + "\n";
        result += "    \"address\": " + "\"" + user.getAddress() + "\"\n";
        result += "}";

        return result;
    }
}
