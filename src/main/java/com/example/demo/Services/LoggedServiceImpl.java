package com.example.demo.Services;

import com.example.demo.Entities.Logged;
import com.example.demo.Entities.User;
import com.example.demo.Repositories.LoggedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LoggedServiceImpl implements LoggedService {
    private final LoggedRepository loggedRepository;

    @Override
    public boolean isLogged(User user) {
        return (loggedRepository.findByUser(user).isValidSession());
    }

    @Override
    public void endSession(User user) {
        Logged logged = loggedRepository.findByUser(user);
        if (Objects.isNull(logged)) {
            logged = new Logged();
            logged.setUser(user);
        }
        logged.setValidSession(false);
        loggedRepository.save(logged);
    }

    @Override
    public void startSession(User user) {
        Logged logged = loggedRepository.findByUser(user);
        if (Objects.isNull(logged)) {
            logged = new Logged();
            logged.setUser(user);
        }
        logged.setLastLogged(new Timestamp((new Date()).getTime()));
        logged.setValidSession(true);
        loggedRepository.save(logged);
    }
}
