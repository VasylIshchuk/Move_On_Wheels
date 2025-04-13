package org.umcs.handlers;

import org.umcs.Creator;
import org.umcs.models.Rental;
import org.umcs.models.User;
import org.umcs.models.Vehicle;
import org.umcs.services.RentalService;
import org.umcs.services.VehicleService;

import java.util.Optional;


public class ClientHandler extends UserHandler {

    public ClientHandler(VehicleService vehicleService, RentalService rentalService) {
        super(vehicleService, rentalService);
    }

    public void handleActions(User user) {
        menuManager.showClientOptions();
        int index = menuManager.getUserNumberFromSelection(menuManager.getStartMenuOptionCount());

        if (index == 1) rentVehicle(user);
        else if (index == 2) returnVehicle(user);
    }

    private void rentVehicle(User user) {
        String vehicleId = getAvailableVehicleId();
        Rental rental = Creator.createRentalFromConsole(user, vehicleId);
        finalizeRental(vehicleId, rental);
    }

    private void finalizeRental(String vehicleId, Rental rental) {
        rentalService.save(rental);
        Optional<Vehicle> vehicle = vehicleService.findById(vehicleId);
        vehicle.get().setRented(true);
        vehicleService.save(vehicle.get());
    }

    private void returnVehicle(User user) {
        Optional<Rental> rental = rentalService.findByUserId(user.getId());
        if (rental.isEmpty()) {
            menuManager.showNoRentalVehiclePrompt();
        } else {
            rentalService.removeRentalById(rental.get().getId());
        }
    }
}
