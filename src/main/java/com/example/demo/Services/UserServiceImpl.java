package com.example.demo.Services;

import com.example.demo.Entities.User;
import com.example.demo.security.JWT;
import com.example.demo.repositories.UserRepository;
import com.example.demo.models.GET.UserDetailsResponse;
import com.example.demo.models.POST.LogInRequest;
import com.example.demo.models.POST.RegistrationRequest;
import com.example.demo.types.AuthorityType;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final LoggedService loggedService;
    private final AuthorityService authorityService;
    private final JWT jwt;

    @Override
    public ResponseEntity<String> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        if (!Objects.isNull(findByUsername(registrationRequest.getUsername()))) {
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
        userRepository.save(newUser);

        authorityService.addRole(newUser, AuthorityType.USER);

        String token = jwt.generate(newUser);
        loggedService.startSession(newUser);

        return ResponseEntity.status(
                HttpStatus.CREATED
        ).body(
                token
        );
    }

    @SneakyThrows
    @Override
    public ResponseEntity<String> logUser(@RequestBody LogInRequest logInRequest) {
        if (Objects.isNull(findByUsername(logInRequest.getUsername()))) {
            return new ResponseEntity<>(
                    "Username not found",
                    HttpStatus.UNAUTHORIZED
            );
        }
        User user = findByUsername(logInRequest.getUsername());
        if (!Objects.equals(logInRequest.getPassword(), user.getPassword())) {
            return new ResponseEntity<>(
                    "Wrong password",
                    HttpStatus.UNAUTHORIZED
            );
        }
        // correct username and password
        String token = jwt.generate(user);

        loggedService.startSession(user);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @Override
    public ResponseEntity<String> logOut(String username) {
        User user = findByUsername(username);
        loggedService.endSession(user);
        System.out.println(user.getUsername() + " logged out.");
        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT
        );
    }

    @Override
    public ResponseEntity<String> userDetails(String username) {
        User user = findByUsername(username);
        UserDetailsResponse userDetails = new UserDetailsResponse(user);

        final HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        Gson gson = new Gson();

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(gson.toJson( userDetails ));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public ResponseEntity<String> setBio(String username, String bio) {
        User user = findByUsername(username);
        user.setBio(bio);
        userRepository.save(user);
        return new ResponseEntity<>(
                HttpStatus.OK
        );
    }
}
