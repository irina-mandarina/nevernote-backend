package com.example.demo.models.GET;

import com.example.demo.Entities.Authority;
import com.example.demo.types.AuthorityType;

public class AuthorityResponse {
    String username;
    AuthorityType role;

    public AuthorityResponse(Authority authority) {
        username = authority.getUser().getUsername();
        role = authority.getRole();
    }
}
