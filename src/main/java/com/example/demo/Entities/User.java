package com.example.demo.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "username")
    @NotNull
    private String username;

    @Column(name = "password")
    @NotNull
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "age")
    private Integer age;

    @Column(name = "bio")
    private String bio;

    @OneToMany
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private List<Note> notes;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private Logged logged;
}
//    create table users (
//        id int primary key auto_increment,
//        username varchar(50),
//    name varchar(75),
//    password varchar(75),
//    age int,
//            address varchar(100),
//            bio varchar(200)
//            );
//
//            create table notes (
//            id int primary key auto_increment,
//            user_id int,
//            title varchar(100),
//            content varchar(500),
//            date datetime
//            );
