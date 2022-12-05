package com.example.demo.Controllers;

import com.example.demo.Requests.POST.LogInRequest;
import com.example.demo.Requests.POST.RegistrationRequest;
import com.example.demo.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:3000")
@AllArgsConstructor
public class UserController {
    UserService userService;

    @PostMapping("/auth/register")
    ResponseEntity<String> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        return userService.registerUser(registrationRequest);
    }

    @PostMapping("/auth/login")
    ResponseEntity<String> logUser(@RequestBody LogInRequest logInRequest) {
        return userService.logUser(logInRequest);
    }

    @PostMapping(("/auth/logout"))
    ResponseEntity<String> logOut(@RequestAttribute String username) {
        return userService.logOut(username);
    }

    @GetMapping("/profile/details")
    ResponseEntity<String> userDetails(@RequestAttribute String username) {
        return userService.userDetails(username);
    }

    @PutMapping("/profile/set-bio")
    ResponseEntity<String> setBio(@RequestAttribute String username, @RequestBody String newBio) {
        return userService.setBio(username, newBio);
    }
}
