package com.example.demo.models.GET;

import com.example.demo.Entities.User;
import lombok.Getter;

@Getter
public class UserDetailsResponse {
    private Long id;

    private String username;

    private String password;

    private String name;

    private String address;

    private Integer age;

    private String bio;

    public UserDetailsResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.name = user.getName();
        this.address = user.getAddress();
        this.age = user.getAge();
        this.bio = user.getBio();
    }
}
