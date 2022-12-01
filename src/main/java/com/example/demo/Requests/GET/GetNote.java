package com.example.demo.Requests.GET;
import com.example.demo.Entities.Note;

import java.util.Objects;

public class GetNote {
    private Long id;
    private String username;
    private String title;
    private String content;
    private String date;
    private String deadline = null;
    private Boolean completed;

    private String privacy;

    public GetNote(Note note) {
        this.id = note.getId();
        this.username = note.getUser().getUsername();
        this.title = note.getTitle();
        this.content = note.getContent();
        this.date = note.getDate().toString();
        if (!Objects.isNull(note.getDeadline())) {
            this.deadline = note.getDeadline().toString();
        }
        this.completed = note.getCompleted();
        this.privacy = note.getPrivacy().name();
    }
}
