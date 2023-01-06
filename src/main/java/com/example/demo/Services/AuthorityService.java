package com.example.demo.Services;

import com.example.demo.Entities.Authority;
import com.example.demo.Entities.User;
import com.example.demo.types.AuthorityType;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AuthorityService {
    ResponseEntity<String> getLoggedUserRoles(User user);

    boolean hasRole(String username, AuthorityType authorityType);

    List<Authority> findAllByUserUsername(String username);

    List<Authority> findAll();

    Authority findByUserUsernameAndAuthority(String username, AuthorityType authority);

    void addRole(User user, AuthorityType role);

    ResponseEntity<String> getAllRoles(List<User> allUsers);

    ResponseEntity<String> removeRole(User loggedUsername, User username, AuthorityType role);

    ResponseEntity<String> addRoleFromLoggedUser(User loggedUser, User user, AuthorityType role);
}
