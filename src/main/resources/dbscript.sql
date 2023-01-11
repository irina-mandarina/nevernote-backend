create database if not exists nevernote;

use nevernote;

create table authorities
(
    user_id int         not null,
    role    varchar(50) not null,
    id      int auto_increment
        primary key
);

create table logs
(
    id         int auto_increment
        primary key,
    user_id    int          not null,
    timestamp  datetime     not null,
    method     varchar(10)  not null,
    path       varchar(50)  not null,
    message    varchar(100) null,
    subject    varchar(50)  null,
    subject_id int          null
);

create table permissions
(
    id                int auto_increment
        primary key,
    user_id           int         not null,
    note_id           int         not null,
    granted_timestamp datetime    not null,
    permission_type   varchar(10) not null
);

create table users
(
    id       int auto_increment
        primary key,
    username varchar(50)  null,
    name     varchar(75)  null,
    password varchar(75)  null,
    age      int          null,
    address  varchar(100) null,
    bio      varchar(200) null
);

create table logged
(
    id             int auto_increment
        primary key,
    last_logged    datetime   null,
    valid_session  tinyint(1) null,
    logged_user_id int        not null,
    constraint logged_user_id
        foreign key (logged_user_id) references users (id)
);

create table notes
(
    id        int auto_increment
        primary key,
    user_id   int          null,
    title     varchar(100) null,
    content   varchar(500) null,
    date      datetime     null,
    deadline  timestamp    null,
    completed tinyint(1)   null,
    privacy   varchar(20)  null,
    constraint user_id
        foreign key (user_id) references users (id)
);

insert into users (username, name, password, age, address, bio)
values ('admin', 'Admin', 'Adm!nPassw0rd', 42, 'Jupiter', 'I am the original administrator');

insert into authorities (user_id, role)
values (1, 'ADMIN');