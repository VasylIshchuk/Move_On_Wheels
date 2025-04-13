package org.umcs.services;

import org.umcs.models.Rental;
import org.umcs.repositories.IRentalRepository;

import java.util.List;
import java.util.Optional;

public class RentalService {
    private final IRentalRepository rentalRepository;

    public RentalService(IRentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public void save(Rental rental) {
        rentalRepository.save(rental);
    }

    public Optional<Rental> findByUserId(String id) {
        return rentalRepository.findByUserId(id);
    }

    public void removeRentalById(String id) {
        rentalRepository.removeRentalById(id);
    }

    public List<Rental> getListRentals() {
        return rentalRepository.getListRentals();
    }
}
