package org.umcs;

import org.umcs.handlers.AdminHandler;
import org.umcs.handlers.AuthenticationHandler;
import org.umcs.handlers.ClientHandler;
import org.umcs.models.User;

public class Application {
    public static void start() {
        User user = authenticateUser();

        if (user.getRole().equals("ADMIN")) {
            while (true) {
                AdminHandler.handleActions();
            }
        } else if (user.getRole().equals("CLIENT")) {
            while (true) {
                ClientHandler.handleActions(user);
            }
        }
    }

    private static User authenticateUser() {
        AuthenticationHandler authenticationHandler = new AuthenticationHandler();
        return authenticationHandler.handleAuthentication();
    }
}
