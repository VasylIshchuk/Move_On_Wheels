package org.umcs.handlers;

import org.umcs.Creator;
import org.umcs.models.Rental;
import org.umcs.models.User;
import org.umcs.models.Vehicle;
import org.umcs.services.RentalService;
import org.umcs.services.UserService;
import org.umcs.services.VehicleService;

import java.util.List;

public class AdminHandler extends UserHandler {
    private final UserService userService;

    public AdminHandler(VehicleService vehicleService, RentalService rentalService, UserService userService) {
        super(vehicleService, rentalService);
        this.userService = userService;
    }

    public void handleActions() {
        menuManager.showAdminOptions();
        int index = menuManager.getUserNumberFromSelection(menuManager.getAdminMenuOptionCount());

        if (index == 1) showListVehicles();
        else if (index == 2) showListRentalVehicles();
        else if (index == 3) addVehicle();
        else if (index == 4) removeVehicle();
        else if (index == 5) showListClients();
    }

    private void showListRentalVehicles() {
        List<Rental> listRental = rentalService.getListRentals();
        menuManager.showList(listRental);
    }

    private void addVehicle() {
        showListVehicles();
        Vehicle vehicle = Creator.createVehicleFromConsole();
        vehicleService.save(vehicle);
    }

    private void removeVehicle() {
        String vehicleId = getAvailableVehicleId();
        vehicleService.deleteById(vehicleId);
    }

    private void showListClients() {
        List<User> listClients = userService.getListClients();
        menuManager.showList(listClients);
    }
}
