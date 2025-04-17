package org.umcs.handlers;

import org.umcs.MenuManager;
import org.umcs.Service;
import org.umcs.models.User;

import java.util.Optional;
import java.util.Scanner;

public class AuthenticationHandler {
    private final Scanner scanner = new Scanner(System.in);
    private final int INDEX_REGISTER_OPTION = 2;
    private final int ADMIN_SELECTION__OPTION = 1;
    private final int CLIENT_SELECTION_OPTION = 2;

    public User handleAuthentication() {
        MenuManager.showAuthenticationMenu();

        int index = MenuManager.getUserNumberFromSelection(MenuManager.START_MENU_OPTION_COUNT);
        if (index == INDEX_REGISTER_OPTION) handleUserRegister();
        return handleUserLogin();
    }

    private User handleUserLogin() {
        Optional<User> user;
        do {
            MenuManager.showLoginHeader();
            String login = getLoginFromUser();
            String password = getPasswordFromUser();
            user = Service.authenticationService.login(login, password);
            if (user.isPresent()) break;
        } while (true);
        return user.get();
    }

    private String getLoginFromUser() {
        MenuManager.showLoginPrompt();
        return scanner.nextLine();
    }

    private String getPasswordFromUser() {
        MenuManager.showPasswordPrompt();
        return scanner.nextLine();
    }

    private void handleUserRegister() {
        do {
            MenuManager.showRegisterHeader();

            String role = getUserRoleFromMenu();
            String login = getNewLoginFromClient();
            String password = getNewPasswordFromClient();
            if (Service.authenticationService.register(login, password, role)) break;
        } while (true);
    }

    private String getUserRoleFromMenu() {
        MenuManager.showStartOptions();

        int index = MenuManager.getUserNumberFromSelection(MenuManager.ROLE_MENU_OPTION_COUNT);
        switch (index) {
            case ADMIN_SELECTION__OPTION:
                return "ADMIN";
            case CLIENT_SELECTION_OPTION:
                return "CLIENT";
        }

        return null;
    }

    private String getNewLoginFromClient() {
        MenuManager.showNewLoginPrompt();
        return scanner.nextLine();
    }

    private String getNewPasswordFromClient() {
        MenuManager.showCreateNewPasswordPrompt();
        return scanner.nextLine();
    }
}
