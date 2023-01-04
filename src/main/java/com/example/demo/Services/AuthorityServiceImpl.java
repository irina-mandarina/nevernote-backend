package com.example.demo.Services;

import com.example.demo.Entities.Authority;
import com.example.demo.Entities.User;
import com.example.demo.Repositories.AuthorityRepository;
import com.example.demo.models.GET.AuthorityResponse;
import com.example.demo.types.AuthorityType;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<String> getRoles(User user) {
        List<Authority> roles = findAllByUserUsername(user.getUsername());
        if (Objects.isNull(roles)) {
            // user has no roles in the db therefore they have only a user role
            addRole(user, AuthorityType.USER);
        }
        // update the list
        roles = findAllByUserUsername(user.getUsername());

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
    public boolean hasRole(String username, AuthorityType authorityType) {
        return (!Objects.isNull(authorityRepository.findByUserUsernameAndRole(username, authorityType)));
    }

    @Override
    public List<Authority> findAllByUserUsername(String username) {
        return authorityRepository.findAllByUserUsername(username);
    }

    @Override
    public Authority findByUserUsernameAndAuthority(String username, AuthorityType authority) {
        return authorityRepository.findByUserUsernameAndRole(username, authority);
    }
}
