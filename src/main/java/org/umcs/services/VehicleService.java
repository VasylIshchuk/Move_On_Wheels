package org.umcs.services;

import org.umcs.models.Vehicle;
import org.umcs.repositories.IVehicleRepository;

import java.util.List;
import java.util.Optional;

public class VehicleService {
    private final IVehicleRepository vehicleRepository;

    public VehicleService(IVehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public void save(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }

    public Optional<Vehicle> findById(String vehicleId) {
        return vehicleRepository.findById(vehicleId);
    }

    public void deleteById(String vehicleId) {
        vehicleRepository.deleteById(vehicleId);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.getAllVehicles();
    }

    public List<Vehicle> getNoRentedVehicles() {
        return vehicleRepository.getNoRentedVehicles();
    }
}
