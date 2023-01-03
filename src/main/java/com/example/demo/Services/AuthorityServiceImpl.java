package com.example.demo.Services;

import com.example.demo.Repositories.AuthorityRepository;
import com.example.demo.types.AuthorityType;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

@AllArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {
    private final AuthorityRepository authorityRepository;

    @Override
    public ResponseEntity<String> getRoles(String username) {
        return null;
    }

    @Override
    public boolean hasRole(String username, AuthorityType authorityType) {
        return (!Objects.isNull(authorityRepository.findByUserUsernameAndAuthority(username, authorityType)));
    }
}
