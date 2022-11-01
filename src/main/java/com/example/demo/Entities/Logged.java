package com.example.demo.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "logged")
public class Logged {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "logged_user_id", referencedColumnName = "id")
    @NotNull
    private User user;

    @Column(name = "last_logged")
    private Timestamp lastLogged;

    @Column(name = "valid_session")
    private boolean validSession;
}
/*


create database logged (
        id int primary key auto_increment,
        last_logged datetime,
        valid_session bool
);
 */
