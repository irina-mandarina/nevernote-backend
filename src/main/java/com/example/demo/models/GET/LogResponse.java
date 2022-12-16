package com.example.demo.models.GET;

import com.example.demo.Entities.Log;
import com.example.demo.types.Method;

import java.sql.Timestamp;

public class LogResponse {
    private Long id;
    private String username;
    private Timestamp timestamp;
    private Method method;
    private String path;
    private String message;
    private String subject;
    private Long subjectId;

    public LogResponse(Log log) {
        this.id = log.getId();
        this.username = log.getUser().getUsername();
        this.timestamp = log.getTimestamp();
        this.method = log.getMethod();
        this.path = log.getPath();
        this.message = log.getMessage();
        this.subject = log.getSubject();
        this.subjectId = log.getSubjectId();
    }
}
