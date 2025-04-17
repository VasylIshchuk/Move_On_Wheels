package org.umcs.services.implementation.standard;

import org.umcs.Creator;
import org.umcs.MenuManager;
import org.umcs.models.Vehicle;
import org.umcs.repositories.IVehicleRepository;
import org.umcs.services.IVehicleService;

import java.util.List;

public class VehicleService implements IVehicleService {
    private final IVehicleRepository vehicleRepository;

    public VehicleService(IVehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public void showListVehicles() {
        List<Vehicle> listVehicles = vehicleRepository.getAllVehicles();
        MenuManager.showList(listVehicles);
    }

    @Override
    public void addVehicle() {
        Vehicle vehicle = Creator.createVehicleFromConsole();
        vehicleRepository.save(vehicle);
    }

    @Override
    public void removeVehicle() {
        Vehicle vehicle = MenuManager.getAvailableVehicleIdFromConsole(vehicleRepository);
        vehicleRepository.deleteById(vehicle.getId());
    }
}
