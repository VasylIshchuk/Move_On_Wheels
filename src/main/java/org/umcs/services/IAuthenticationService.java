package org.umcs.services;

import org.umcs.models.User;

import java.util.Optional;

public interface IAuthenticationService {
    Optional<User> login(String login, String password);
    boolean register(String login, String password, String role);
}
