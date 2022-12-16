package com.example.demo.Controllers;

import com.example.demo.Services.LogService;
import com.example.demo.models.POST.LogInRequest;
import com.example.demo.models.POST.RegistrationRequest;
import com.example.demo.Services.UserService;
import com.example.demo.types.Method;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:3000")
@AllArgsConstructor
public class UserController {
    UserService userService;
    LogService logService;

    @PostMapping("/auth/register")
    ResponseEntity<String> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        ResponseEntity<String> response = userService.registerUser(registrationRequest);
        logService.log(response, registrationRequest.getUsername(), Method.POST, "/auth/register");
        return response;
    }

    @PostMapping("/auth/login")
    ResponseEntity<String> logUser(@RequestBody LogInRequest logInRequest) {
        ResponseEntity<String> response = userService.logUser(logInRequest);
        logService.log(response, logInRequest.getUsername(), Method.POST, "/auth/login");
        return response;
    }

    @PostMapping(("/auth/logout"))
    ResponseEntity<String> logOut(@RequestAttribute String username) {
        ResponseEntity<String> response = userService.logOut(username);
        logService.log(response, username, Method.POST, "/auth/logout");
        return response;
    }

    @GetMapping("/profile/details")
    ResponseEntity<String> userDetails(@RequestAttribute String username) {
        ResponseEntity<String> response = userService.userDetails(username);
        logService.log(response, username, Method.GET, "/profile/details");
        return response;
    }

    @PutMapping("/profile/set-bio")
    ResponseEntity<String> setBio(@RequestAttribute String username, @RequestBody String newBio) {
        ResponseEntity<String> response = userService.setBio(username, newBio);
        logService.log(response, username, Method.PUT, "/profile/set-bio");
        return response;
    }
}
