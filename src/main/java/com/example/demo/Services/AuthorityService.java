package com.example.demo.Services;

import com.example.demo.Entities.Authority;
import com.example.demo.Entities.User;
import com.example.demo.types.AuthorityType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AuthorityService {
    ResponseEntity<String> getRoles(User user);

    void addRole(User user, AuthorityType role);

    boolean hasRole(String username, AuthorityType authorityType);

    List<Authority> findAllByUserUsername(String username);

    Authority findByUserUsernameAndAuthority(String username, AuthorityType authority);
}
