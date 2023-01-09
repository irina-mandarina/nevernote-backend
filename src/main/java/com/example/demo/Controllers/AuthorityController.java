package com.example.demo.Controllers;

import com.example.demo.Services.AuthorityService;
import com.example.demo.Services.LogService;
import com.example.demo.Services.UserService;
import com.example.demo.types.AuthorityType;
import com.example.demo.types.Method;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:3000")
@AllArgsConstructor
public class AuthorityController {
    private AuthorityService authorityService;
    private UserService userService;
    private LogService logService;

    @GetMapping("/auth/roles")
    ResponseEntity<String> getRolesForLoggedUser(@RequestAttribute String username) {
        ResponseEntity<String> response = authorityService.getLoggedUserRoles(userService.findByUsername(username));
        logService.log(response, username, Method.GET, "/auth/roles");
        return response;
    }

    @GetMapping("/users/roles")
    ResponseEntity<String> getAllRoles(@RequestAttribute String username) {
        ResponseEntity<String> response = authorityService.getAllRoles(userService.findAll());
        logService.log(response, username, Method.GET, "/users/roles");
        return response;
    }

    @PostMapping("/users/{usernameGettingRole}/roles")
    ResponseEntity<String> addRole(@RequestAttribute String username, @RequestBody AuthorityType role, @PathVariable String usernameGettingRole) {
        ResponseEntity<String> response = authorityService.addRoleFromLoggedUser(userService.findByUsername(username), userService.findByUsername(usernameGettingRole), role);
        logService.log(response, username, Method.POST, "/users/" + usernameGettingRole + "/roles");
        return response;
    }

    @DeleteMapping("/users/{usernameLosingRole}/roles")
    ResponseEntity<String> removeRole(@RequestAttribute String username, @RequestParam AuthorityType role, @PathVariable String usernameLosingRole) {
        ResponseEntity<String> response = authorityService.removeRole(userService.findByUsername(username), userService.findByUsername(usernameLosingRole), role);
        logService.log(response, username, Method.DELETE, "/users/" + usernameLosingRole + "/roles");
        return response;
    }
}
