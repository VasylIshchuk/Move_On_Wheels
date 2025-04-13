package org.umcs.repositories;

import org.umcs.models.Rental;

import java.util.List;
import java.util.Optional;

public interface IRentalRepository {
    void save(Rental rental);

    void removeRentalById(String rentalId);

    Optional<Rental> findByUserId(String userId);

    List<Rental> getListRentals();
}
