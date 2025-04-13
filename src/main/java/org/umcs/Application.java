package org.umcs;

import org.umcs.handlers.AdminHandler;
import org.umcs.handlers.AuthenticationHandler;
import org.umcs.handlers.ClientHandler;
import org.umcs.repositories.implementation.json.JsonRentalRepository;
import org.umcs.repositories.implementation.json.JsonUserRepository;
import org.umcs.repositories.implementation.json.JsonVehicleRepository;
import org.umcs.models.User;
import org.umcs.services.AuthenticationService;
import org.umcs.services.RentalService;
import org.umcs.services.UserService;
import org.umcs.services.VehicleService;

public class Application {
    public static void start(AuthenticationService authenticationService, VehicleService vehicleService, RentalService rentalService, UserService userService) {
        User user = authenticateUser(authenticationService);

        if (user.getRole().equals("Admin")) {
            while (true) {
                AdminHandler adminHandler = new AdminHandler(vehicleService, rentalService, userService);
                adminHandler.handleActions();
            }
        } else if (user.getRole().equals("Client")) {
            while (true) {
                ClientHandler clientHandler = new ClientHandler(vehicleService, rentalService);
                clientHandler.handleActions(user);
            }
        }
    }

    private static User authenticateUser(AuthenticationService authenticationService) {
        AuthenticationHandler authenticationHandler = new AuthenticationHandler(authenticationService);
        return authenticationHandler.handleAuthentication();
    }

}
