package com.example.demo.Services;

import com.example.demo.Entities.User;
import com.example.demo.models.POST.LogInRequest;
import com.example.demo.models.POST.RegistrationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

public interface UserService {
    ResponseEntity<String> registerUser(@RequestBody RegistrationRequest registrationRequest);
    ResponseEntity<String> logUser(@RequestBody LogInRequest logInRequest);
    ResponseEntity<String> logOut(String username);
    ResponseEntity<String> userDetails(String username);
    User findByUsername(String username);
    List<User> findAll();
    ResponseEntity<String> setBio(@RequestHeader String username, @RequestBody String bio);
}
