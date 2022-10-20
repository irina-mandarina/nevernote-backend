package com.example.demo.Requests.GET;

import com.example.demo.Entities.User;
import com.example.demo.Services.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class GetUserDetails {
    private Long id;

    private String username;

    private String password;

    private String name;

    private String address;

    private Integer age;

    private String bio;

    public GetUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.name = user.getName();
        this.address = user.getAddress();
        this.age = user.getAge();
    }
}
