package com.example.demo.Controllers;

import com.example.demo.Requests.POST.LogInRequest;
import com.example.demo.Requests.POST.RegistrationRequest;
import com.example.demo.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
    ResponseEntity<String> logOut(@RequestHeader String username) {
        return userService.logOut(username);
    }
}
