package com.example.demo.models.POST;

import com.example.demo.types.AuthorityType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class RegistrationRequest {
    String name;
    Integer age;
    String address;
    String username;
    String password;
    AuthorityType roles[] ;
}
