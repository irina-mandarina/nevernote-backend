package com.example.demo.models.GET;

import com.example.demo.Entities.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GetNotes {
    List <NoteResponse> getNotes;

    public GetNotes(List<Note> notes) {
        if (Objects.isNull(notes)) {
            return;
        }
        getNotes = new ArrayList<>(100);
        for (Note note: notes) {
            NoteResponse getNote = new NoteResponse(note);
            getNotes.add(getNote);
        }
    }
}
