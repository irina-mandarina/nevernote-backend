package com.example.demo.Repositories;

import com.example.demo.Entities.Authority;
import com.example.demo.types.AuthorityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    List<Authority> findAllByUserUsername(String username);

    Authority findByUserUsernameAndRole(String username, AuthorityType authority);
}
