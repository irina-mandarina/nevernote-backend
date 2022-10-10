package Services;

import Entities.User;
import Requests.POST.LogInRequest;
import Requests.POST.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import Repositories.Store;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final Store store;

    @Override
    public ResponseEntity<String> registerUser(RegistrationRequest registrationRequest) {
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
        return new ResponseEntity<>(
                HttpStatus.CREATED
        );
    }

    @Override
    public ResponseEntity<String> logUser(LogInRequest logInRequest) {
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
        return new ResponseEntity<>(
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<String> logOut() {
        store.logOut();
        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT
        );
    }
}
