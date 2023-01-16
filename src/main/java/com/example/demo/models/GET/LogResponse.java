package com.example.demo.models.GET;

import com.example.demo.Entities.Log;
import com.example.demo.types.Method;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
public class LogResponse {
    @Id
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
