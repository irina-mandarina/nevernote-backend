package Services;

import Requests.POST.LogInRequest;
import Requests.POST.RegistrationRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<String> registerUser(RegistrationRequest registrationRequest);
    ResponseEntity<String> logUser(LogInRequest logInRequest);

    ResponseEntity<String> logOut();
}
