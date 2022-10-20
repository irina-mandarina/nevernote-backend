package com.example.demo.Services;

import com.example.demo.Entities.User;

public interface LoggedService {

    boolean isLogged(User user);

    void endSession(User user);

    void startSession(User user);
}
