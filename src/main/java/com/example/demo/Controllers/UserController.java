package com.example.demo.Controllers;

import com.example.demo.Requests.POST.LogInRequest;
import com.example.demo.Requests.POST.RegistrationRequest;
import com.example.demo.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class UserController {
    UserService userService;

    @CrossOrigin
    @PostMapping("/auth/register")
    ResponseEntity<String> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        System.out.println("odbfsirfb");
        return userService.registerUser(registrationRequest);
    }

    @CrossOrigin
    @PostMapping("/auth/login")
    ResponseEntity<String> logUser(@RequestBody LogInRequest logInRequest) {
        return userService.logUser(logInRequest);
    }

    @CrossOrigin
    @PostMapping(("/auth/logout"))
    ResponseEntity<String> logOut(@RequestHeader String username) {
        return userService.logOut(username);
    }

    @CrossOrigin
    @GetMapping("/profile/details")
    ResponseEntity<String> userDetails(@RequestHeader String username) {
        return userService.userDetails(username);
    }

    @CrossOrigin
    @PutMapping("/profile/setbio")
    ResponseEntity<String> setBio(@RequestHeader String username, @RequestBody String newBio) {
        return userService.setBio(username, newBio);
    }
}
