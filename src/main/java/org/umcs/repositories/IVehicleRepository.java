package org.umcs.repositories;

import org.umcs.models.Vehicle;

import java.util.List;
import java.util.Optional;

public interface IVehicleRepository {

    void save(Vehicle vehicle);


    void deleteById(String vehicleId);

    Optional<Vehicle> findById(String vehicleId);

    List<Vehicle> getNoRentedVehicles();

    List<Vehicle> getAllVehicles();
}
