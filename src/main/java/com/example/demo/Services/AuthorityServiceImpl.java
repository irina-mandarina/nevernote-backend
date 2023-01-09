package com.example.demo.Services;

import com.example.demo.Entities.Authority;
import com.example.demo.Entities.User;
import com.example.demo.Repositories.AuthorityRepository;
import com.example.demo.models.GET.AuthorityResponse;
import com.example.demo.types.AuthorityType;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {
    private final AuthorityRepository authorityRepository;

    @Override
    public ResponseEntity<String> getLoggedUserRoles(User user) {
        List<Authority> roles = findAllByUserUsername(user.getUsername());
        if (roles.size() == 0) {
            // user has no roles in the db therefore they have only a user role
            addRole(user, AuthorityType.USER);
        }
        // update the list
        roles = findAllByUserUsername(user.getUsername());

        return getStringResponseEntity(roles);
    }

    @Override
    public void addRole(User user, AuthorityType role) {
        if (hasRole(user.getUsername(), role)) {
            return;
        }
        Authority authority = new Authority();
        authority.setRole(role);
        authority.setUser(user);
        authorityRepository.save(authority);
    }

    @Override
    public ResponseEntity<String> getAllRoles(List<User> allUsers) {
        List<Authority> roles = findAll();
        if (roles.size() != allUsers.size()) {
            // there are missing roles in the db
            for (User user: allUsers) {
                // if the user has no roles they should have USER
                if (findAllByUserUsername(user.getUsername()).isEmpty()) {
                    addRole(user, AuthorityType.USER);
                }
            }
        }
        // update the list
        roles = findAll();

        return getStringResponseEntity(roles);
    }

    @Override
    public ResponseEntity<String> removeRole(User loggedUser, User user, AuthorityType role) {
//        if (Objects.isNull(findByUserUsernameAndAuthority(loggedUser.getUsername(), AuthorityType.ADMIN))) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//        }
        Authority authority = findByUserUsernameAndAuthority(user.getUsername(), role);
        if (authority.getRole().equals(AuthorityType.USER) && findAllByUserUsername(user.getUsername()).size() == 1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("The only role they have cannot be removed.");
        }
//        if (Objects.isNull(authority)) {
//            return ResponseEntity.status(HttpStatus.NO_CONTENT)
//                    .body("User " + user.getUsername() + " does not own " + role.name());
//        }
        authorityRepository.delete(authority);
        return getStringResponseEntity(findAllByUserUsername(user.getUsername()));
    }

    @Override
    public ResponseEntity<String> addRoleFromLoggedUser(User loggedUser, User user, AuthorityType role) {
//        if (Objects.isNull(findByUserUsernameAndAuthority(loggedUser.getUsername(), AuthorityType.ADMIN))) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//        }
        addRole(user, role);
        return getStringResponseEntity(findAllByUserUsername(user.getUsername()));
    }

    private ResponseEntity<String> getStringResponseEntity(List<Authority> roles) {
        List<AuthorityResponse> authorityResponseList = new ArrayList<>();
        for(Authority authority: roles) {
            authorityResponseList.add(new AuthorityResponse(authority));
        }

        Gson gson = new Gson();
        return ResponseEntity.ok().body(
                gson.toJson(authorityResponseList)
        );
    }

    @Override
    public boolean hasRole(String username, AuthorityType authorityType) {
        return (!Objects.isNull(authorityRepository.findByUserUsernameAndRole(username, authorityType)));
    }

    @Override
    public List<Authority> findAllByUserUsername(String username) {
        return authorityRepository.findAllByUserUsername(username);
    }

    @Override
    public List<Authority> findAll() {
        return authorityRepository.findAll();
    }

    @Override
    public Authority findByUserUsernameAndAuthority(String username, AuthorityType authority) {
        return authorityRepository.findByUserUsernameAndRole(username, authority);
    }

}
