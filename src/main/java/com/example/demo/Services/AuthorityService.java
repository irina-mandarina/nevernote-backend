package com.example.demo.Services;

import com.example.demo.types.AuthorityType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AuthorityService {
    ResponseEntity<String> getRoles(String username);
    boolean hasRole(String username, AuthorityType authorityType);
}
