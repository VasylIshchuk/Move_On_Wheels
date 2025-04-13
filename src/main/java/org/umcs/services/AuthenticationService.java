package org.umcs.services;

import org.mindrot.jbcrypt.BCrypt;
import org.umcs.MenuManager;
import org.umcs.models.User;
import org.umcs.repositories.IUserRepository;

import java.util.Optional;

public class AuthenticationService {
    private final IUserRepository userRepository;
    private final MenuManager menuManager = new MenuManager();

    public AuthenticationService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> login(String login, String password) {
        Optional<User> user = userRepository.findByLogin(login);

        if (user.isEmpty()) {
            menuManager.showInvalidLoginPrompt();
            return Optional.empty();
        } else if (!validatePassword(password, user.get().getPassword())) {
            menuManager.showInvalidPasswordPrompt();
            return Optional.empty();
        }

        return user;
    }

    private boolean validatePassword(String password, String passwordFromFile) {
        return BCrypt.checkpw(password, passwordFromFile);
    }

    public boolean register(String login, String password, String role) {
        if (!userRepository.validateUserLogin(login)) {
            menuManager.showLoginExistsPrompt();
            return false;
        }

        String hashedPassword = hashPassword(password);
        User user = new User(login, hashedPassword, role);

        userRepository.save(user);
        return true;
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
