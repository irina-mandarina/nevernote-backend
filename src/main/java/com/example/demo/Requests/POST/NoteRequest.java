package com.example.demo.Requests.POST;

import com.example.demo.types.Privacy;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class NoteRequest {
    String title;
    String content;
    Timestamp deadline;
    Privacy privacy = Privacy.PRIVATE;
}
