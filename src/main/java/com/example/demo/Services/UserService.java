package com.example.demo.Services;

import com.example.demo.Requests.POST.LogInRequest;
import com.example.demo.Requests.POST.RegistrationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface UserService {
    ResponseEntity<String> registerUser(@RequestBody RegistrationRequest registrationRequest);
    ResponseEntity<String> logUser(@RequestBody LogInRequest logInRequest);

    ResponseEntity<String> logOut(@RequestHeader String username);
}
