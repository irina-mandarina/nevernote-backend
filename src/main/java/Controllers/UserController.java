package Controllers;

import Requests.POST.LogInRequest;
import Requests.POST.RegistrationRequest;
import Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {
    UserService userService;

    @PostMapping("/auth/register")
    ResponseEntity<String> registerUser(RegistrationRequest registrationRequest) {
        return userService.registerUser(registrationRequest);
    }

    @PostMapping("/auth/login")
    ResponseEntity<String> logUser(LogInRequest logInRequest) {
        return userService.logUser(logInRequest);
    }

    @PostMapping(("/auth/logout"))
    ResponseEntity<String> logOut() {
        return userService.logOut();
    }
}
