package org.umcs.handlers;

import org.umcs.services.AuthenticationService;
import org.umcs.MenuManager;
import org.umcs.models.User;

import java.util.Optional;
import java.util.Scanner;

public class AuthenticationHandler {
    private final AuthenticationService authenticationService;

    private final MenuManager menuManager = new MenuManager();
    private final Scanner scanner = new Scanner(System.in);

    public AuthenticationHandler(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public User handleAuthentication() {
        menuManager.showAuthenticationMenu();

        int index = menuManager.getUserNumberFromSelection(menuManager.getStartMenuOptionCount());
        if (index == menuManager.getIndexRegisterOption()) handleUserRegister();
        return handleUserLogin();
    }

    private User handleUserLogin() {
        Optional<User> user;
        do {
            menuManager.showLoginHeader();
            String login = getLoginFromUser();
            String password = getPasswordFromUser();
            user = authenticationService.login(login, password);
            if (user.isPresent()) break;
        } while (true);
        return user.get();
    }

    private String getLoginFromUser() {
        menuManager.showLoginPrompt();
        return scanner.nextLine();
    }

    private String getPasswordFromUser() {
        menuManager.showPasswordPrompt();
        return scanner.nextLine();
    }

    private void handleUserRegister() {
        do {
            menuManager.showRegisterHeader();

            String role = getUserRoleFromMenu();
            String login = getNewLoginFromClient();
            String password = getNewPasswordFromClient();
            if (authenticationService.register(login, password, role)) break;
        } while (true);
    }

    private String getUserRoleFromMenu() {
        menuManager.showStartOptions();

        int index = menuManager.getUserNumberFromSelection(menuManager.getStartMenuOptionCount());
        switch (index) {
            case 1:
                return "Admin";
            case 2:
                return "Client";
        }
        return null;
    }

    private String getNewLoginFromClient() {
        menuManager.showNewLoginPrompt();
        return scanner.nextLine();
    }

    private String getNewPasswordFromClient() {
        menuManager.showCreateNewPasswordPrompt();
        return scanner.nextLine();
    }
}
