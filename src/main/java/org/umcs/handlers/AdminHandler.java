package org.umcs.handlers;

import org.umcs.MenuManager;
import org.umcs.Service;
import org.umcs.models.User;
import org.umcs.services.IRentalService;
import org.umcs.services.IUserService;
import org.umcs.services.IVehicleService;
import org.umcs.services.implementation.standard.RentalService;
import org.umcs.services.implementation.standard.UserService;
import org.umcs.services.implementation.standard.VehicleService;

import java.util.List;

public class AdminHandler {
    public static void handleActions() {
        MenuManager.showAdminOptions();
        int index = MenuManager.getUserNumberFromSelection(MenuManager.ADMIN_MENU_OPTION_COUNT);

        if (index == 1) Service.vehicleService.showListVehicles();
        else if (index == 2) Service.rentalService.showListRentalVehicles();
        else if (index == 3) Service.vehicleService.addVehicle();
        else if (index == 4) Service.vehicleService.removeVehicle();
        else if (index == 5) Service.userService.showListClients();
    }
}
