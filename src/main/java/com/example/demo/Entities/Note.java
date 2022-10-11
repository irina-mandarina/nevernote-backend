package com.example.demo.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "notes")
public class Note {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne()
//    @JoinColumn(name = "user_id")
//    @NotNull
    private User user;

//    @Column(name = "title")
    private String title;

//    @Column(name = "content")
    private String content;

//    @Column(name = "date", nullable = false)
//    @NotNull
    private Timestamp date;

//    public String toString() {
//        String noteStr = "{";
//        noteStr += "id: " + id.toString();
//        noteStr += "\n";
//        noteStr += "";
//    }
}
