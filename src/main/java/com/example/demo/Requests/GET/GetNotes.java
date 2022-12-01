package com.example.demo.Requests.GET;

import com.example.demo.Entities.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GetNotes {
    List <GetNote> getNotes;

    public GetNotes(List<Note> notes) {
        if (Objects.isNull(notes)) {
            return;
        }
        getNotes = new ArrayList<>(100);
        for (Note note: notes) {
            GetNote getNote = new GetNote(note);
            getNotes.add(getNote);
        }
    }
}
