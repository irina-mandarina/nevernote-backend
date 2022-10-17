package com.example.demo.Services;

import com.example.demo.Entities.User;
import com.example.demo.Requests.POST.LogInRequest;
import com.example.demo.Requests.POST.RegistrationRequest;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import com.example.demo.Repositories.Store;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final Store store;

    @Override
    public ResponseEntity<String> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        if (store.usernameExists(registrationRequest.getUsername())) {
            return new ResponseEntity<>(
                    "Username is taken",
                    HttpStatus.BAD_REQUEST
            );
        }
        User newUser = new User();
        newUser.setUsername(registrationRequest.getUsername());
        newUser.setName(registrationRequest.getName());
        newUser.setAddress(registrationRequest.getAddress());
        newUser.setAge(registrationRequest.getAge());
        newUser.setPassword(registrationRequest.getPassword());
        store.saveUser(newUser);
        store.logUser(newUser.getUsername());

        return new ResponseEntity<>(
                HttpStatus.CREATED
        );
    }

    @Override
    public ResponseEntity<String> logUser(@RequestBody LogInRequest logInRequest) {
        if (!store.usernameExists(logInRequest.getUsername())) {
            return new ResponseEntity<>(
                    "Username not found",
                    HttpStatus.UNAUTHORIZED
            );
        }
        User user = store.findUserByUsername(logInRequest.getUsername());
        if (!Objects.equals(logInRequest.getPassword(), user.getPassword())) {
            return new ResponseEntity<>(
                    "Wrong password",
                    HttpStatus.UNAUTHORIZED
            );
        }
        store.logUser(user.getUsername());
        return new ResponseEntity<>(
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<String> logOut(String username) {
        store.logOut(username);
        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT
        );
    }

    @Override
    public ResponseEntity<String> userDetails(@RequestHeader String username) {
        if (username.isEmpty() || !store.usernameExists(username)) {
            return new ResponseEntity<>(
                    HttpStatus.UNAUTHORIZED
            );
        }

        final HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        Gson gson = new Gson();

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(gson.toJson(store.findUserByUsername(username)));
    }
}
