package com.example.demo.Services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.example.demo.Entities.User;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Requests.GET.GetUserDetails;
import com.example.demo.Requests.POST.LogInRequest;
import com.example.demo.Requests.POST.RegistrationRequest;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
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
    private final UserRepository userRepository;
    private final LoggedService loggedService;

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
        loggedService.startSession(newUser);

        return new ResponseEntity<>(
                HttpStatus.CREATED
        );
    }

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
        try {
            Algorithm algorithm = Algorithm.HMAC256("hmaKey");
            String token = JWT.create()
                    .withIssuer("auth0")
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            // Invalid Signing configuration / Couldn't convert Claims.
        }

        /*
        HttpResponse<String> response = Unirest.post("https://dev-vnwxm6hjksh0tszk.us.auth0.com/oauth/token")
                .header("content-type", "application/x-www-form-urlencoded")
                .body("grant_type=client_credentials&client_id=%24%7Baccount.clientId%7D&client_secret=YOUR_CLIENT_SECRET&audience=YOUR_API_IDENTIFIER")
                .asString();
*/

        loggedService.startSession(user);
        return new ResponseEntity<>(
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<String> logOut(String username) {
        loggedService.endSession(findByUsername(username));
        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT
        );
    }

    @Override
    public ResponseEntity<String> userDetails(@RequestHeader String username) {
        if (username.isEmpty() || Objects.isNull(findByUsername(username)) ) {
            return new ResponseEntity<>(
                    HttpStatus.UNAUTHORIZED
            );
        }

        User user = findByUsername(username);
        GetUserDetails userDetails = new GetUserDetails(user);

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
    public ResponseEntity<String> setBio(String username, String bio) {
        User user = findByUsername(username);
        user.setBio(bio);
        userRepository.save(user);
        return new ResponseEntity<>(
                HttpStatus.OK
        );
    }
}
