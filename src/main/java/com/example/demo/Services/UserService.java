package com.example.demo.Services;

import com.example.demo.Entities.User;
import com.example.demo.Requests.POST.LogInRequest;
import com.example.demo.Requests.POST.RegistrationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.security.Principal;

public interface UserService {
    ResponseEntity<String> registerUser(@RequestBody RegistrationRequest registrationRequest);
    ResponseEntity<String> logUser(@RequestBody LogInRequest logInRequest);

    ResponseEntity<String> logOut(String username);

    ResponseEntity<String> userDetails(String username);

    User findByUsername(String username);

    ResponseEntity<String> setBio(@RequestHeader String username, @RequestBody String bio);
}
