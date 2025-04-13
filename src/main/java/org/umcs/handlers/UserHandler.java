package org.umcs.handlers;

import org.umcs.MenuManager;
import org.umcs.models.Vehicle;
import org.umcs.services.RentalService;
import org.umcs.services.VehicleService;

import java.util.List;
import java.util.Optional;

public class UserHandler {
    protected final VehicleService vehicleService;
    protected final RentalService rentalService;
    protected final MenuManager menuManager = new MenuManager();

    public UserHandler(VehicleService vehicleService, RentalService rentalService) {
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
    }

    protected void showListVehicles() {
        List<Vehicle> listVehicles = vehicleService.getAllVehicles();
        menuManager.showList(listVehicles);
    }

    protected String getAvailableVehicleId() {
        do {
            List<Vehicle> availableVehicles = vehicleService.getNoRentedVehicles();
            menuManager.showList(availableVehicles);

            String vehicleId = menuManager.getIdFromConsole();

            Optional<Vehicle> vehicle = vehicleService.findById(vehicleId);
            if (vehicle.isPresent()) return vehicleId;
            menuManager.showInvalidIdPrompt();
        } while (true);
    }
}
