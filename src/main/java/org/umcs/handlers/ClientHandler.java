package org.umcs.handlers;

import org.umcs.MenuManager;
import org.umcs.Service;
import org.umcs.models.User;

public class ClientHandler {
    public static void handleActions(User user) {
        MenuManager.showClientOptions();
        int index = MenuManager.getUserNumberFromSelection(MenuManager.CLIENT_MENU_OPTION_COUNT);

        if (index == 1) Service.rentalService.rentVehicle(user);
        else if (index == 2) Service.rentalService.returnVehicle(user);
    }
}
