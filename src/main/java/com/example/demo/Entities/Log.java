package com.example.demo.Entities;

import com.example.demo.types.Method;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "logs")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @NotNull
    private User user;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Column(name = "method")
    private Method method;

    @Column(name = "path")
    private String path;

    @Column(name = "message")
    private String message;

    @Column(name = "subject")
    private String subject;

    @Column(name = "subject_id")
    private Long subjectId;

}
