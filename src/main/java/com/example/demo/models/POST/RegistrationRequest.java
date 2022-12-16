package com.example.demo.models.POST;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RegistrationRequest {
    String name;
    Integer age;
    String address;
    String username;
    String password;
}
