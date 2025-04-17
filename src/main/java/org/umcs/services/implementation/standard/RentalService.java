package org.umcs.services.implementation.standard;

import org.umcs.MenuManager;
import org.umcs.models.Rental;
import org.umcs.models.User;
import org.umcs.models.Vehicle;
import org.umcs.repositories.IRentalRepository;
import org.umcs.repositories.IVehicleRepository;
import org.umcs.services.IRentalService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class RentalService implements IRentalService {
    private final IRentalRepository rentalRepository;
    private final IVehicleRepository vehicleRepository;

    public RentalService(IVehicleRepository vehicleRepository, IRentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public void showListRentalVehicles() {
        List<Rental> listRental = rentalRepository.getAllRentals();
        MenuManager.showList(listRental);
    }

    @Override
    public void rentVehicle(User user) {
        if(userHasRental(user.getId())) {
            MenuManager.UserAlreadyHasRentedVehicle();
            return;
        }

        Vehicle vehicle = MenuManager.getAvailableVehicleIdFromConsole(vehicleRepository);
        String currentDate = getCurrentDate();
        Rental rental = new Rental(vehicle, user, currentDate);

        finalizeRental(vehicle, rental);
    }
    private boolean userHasRental(String userId){
        Optional<Rental> optionalRental = rentalRepository.findByUserIdAndReturnDateIsNull(userId);
        return  optionalRental.isPresent();
    }

    private String getCurrentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

    private void finalizeRental(Vehicle vehicle, Rental rental) {
        rentalRepository.save(rental);
        vehicle.setRented(true);
        vehicleRepository.save(vehicle);
    }

    @Override
    public void returnVehicle(User user) {
        Optional<Rental> optionalRental = rentalRepository.findByUserIdAndReturnDateIsNull(user.getId());

        if (optionalRental.isEmpty()) {
            MenuManager.showNoRentalVehiclePrompt();
        } else {
            Rental rental = optionalRental.get();
            finalizeRentalReturnDate(rental);
            markVehicleAsAvailable(rental);
        }
    }

    private void finalizeRentalReturnDate(Rental rental) {
        String currentDate = getCurrentDate();
        rental.setReturnDate(currentDate);
        rentalRepository.save(rental);
    }

    private void markVehicleAsAvailable(Rental rental) {
        Vehicle vehicle = rental.getVehicle();
        vehicle.setRented(false);
        vehicleRepository.save(vehicle);
    }
}
