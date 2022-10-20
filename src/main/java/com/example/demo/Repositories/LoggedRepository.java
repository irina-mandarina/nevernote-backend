package com.example.demo.Repositories;

import com.example.demo.Entities.Logged;
import com.example.demo.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggedRepository extends JpaRepository<Logged, Long> {
    Logged findByUser(User user);
}
