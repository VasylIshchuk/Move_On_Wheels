package org.umcs.services.implementation.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.umcs.MenuManager;
import org.umcs.models.Rental;
import org.umcs.models.User;
import org.umcs.models.Vehicle;
import org.umcs.repositories.implementation.hibernate.HibernateRentalRepository;
import org.umcs.repositories.implementation.hibernate.HibernateVehicleRepository;
import org.umcs.services.IRentalService;
import org.umcs.storage.HibernateConfig;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class RentalHibernateService implements IRentalService {
    private final HibernateVehicleRepository vehicleRepository;
    private final HibernateRentalRepository rentalRepository;

    public RentalHibernateService(HibernateVehicleRepository vehicleRepository, HibernateRentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public void showListRentalVehicles() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            rentalRepository.setSession(session);
            List<Rental> listRental = rentalRepository.getAllRentals();
            MenuManager.showList(listRental);
        }
    }

    @Override
    public void rentVehicle(User user) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            rentalRepository.setSession(session);
            vehicleRepository.setSession(session);
            processRent(user);
            transaction.commit();
        }
    }

    private void processRent(User user) {
        if (userHasRental(user.getId())) {
            MenuManager.UserAlreadyHasRentedVehicle();
            return;
        }

        Vehicle vehicle = MenuManager.getAvailableVehicleIdFromConsole(vehicleRepository);
        String currentDate = getCurrentDate();
        Rental rental = new Rental(vehicle, user, currentDate);

        finalizeRental(vehicle, rental);
    }

    private boolean userHasRental(String userId) {
        Optional<Rental> optionalRental = rentalRepository.findByUserIdAndReturnDateIsNull(userId);
        return optionalRental.isPresent();
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
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            rentalRepository.setSession(session);
            vehicleRepository.setSession(session);
            processReturn(user);
            transaction.commit();
        }
    }

    private void processReturn(User user) {
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
