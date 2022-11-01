package com.example.demo.Requests.POST;

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
