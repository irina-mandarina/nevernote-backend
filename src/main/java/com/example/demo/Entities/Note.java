package com.example.demo.Entities;

import com.example.demo.types.Privacy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notes")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    @JsonIgnore
    private User user;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "date", nullable = false)
    @NotNull
    private Timestamp date;

    @Column(name = "deadline")
    private Timestamp deadline;

    @Column(name = "completed")
    private Boolean completed = false;

    @Column(name = "privacy")
    private Privacy privacy = Privacy.PRIVATE;

}
