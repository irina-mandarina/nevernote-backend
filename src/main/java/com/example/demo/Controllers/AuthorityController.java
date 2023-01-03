package com.example.demo.Controllers;

import com.example.demo.Services.AuthorityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("http://localhost:3000")
@AllArgsConstructor
public class AuthorityController {
    private final AuthorityService authorityService;

    @GetMapping("/auth/roles")
    ResponseEntity<String> getRoles(@RequestAttribute String username) {
        ResponseEntity<String> response = authorityService.getRoles(username);
        return response;
    }
}
