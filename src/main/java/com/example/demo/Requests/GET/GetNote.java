package com.example.demo.Requests.GET;
import com.example.demo.Entities.Note;

import java.sql.Timestamp;

public class GetNote {
    private Long id;

    private String username;

    private String title;

    private String content;

    private Timestamp date;

    public GetNote(Note note) {
        this.id = note.getId();
        this.username = note.getUser().getUsername();
        this.title = note.getTitle();
        this.content = note.getContent();
        this.date = note.getDate();
    }
}
