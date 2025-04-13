package org.umcs.repositories.implementation.json;

import com.google.gson.reflect.TypeToken;
import org.umcs.storage.JsonFileStorage;
import org.umcs.models.Vehicle;
import org.umcs.repositories.IVehicleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JsonVehicleRepository implements IVehicleRepository {
    private static JsonVehicleRepository instance;

    private final List<Vehicle> vehicles;
    private final String filePath = "vehicles.json";
    private final JsonFileStorage<Vehicle> jsonFileStorage = new JsonFileStorage<>(filePath);

    private JsonVehicleRepository() {
        this.vehicles = jsonFileStorage.load(new TypeToken<List<Vehicle>>() {
        }.getType());
    }

    public static JsonVehicleRepository getInstance() {
        if (instance == null) {
            instance = new JsonVehicleRepository();
        }
        return instance;
    }

    @Override
    public void deleteById(String vehicleId) {
        vehicles.removeIf(vehicle -> vehicle.getId().equals(vehicleId));
        jsonFileStorage.saveToFile(vehicles);
    }

    @Override
    public void save(Vehicle vehicle) {
        vehicles.add(vehicle);
        jsonFileStorage.saveToFile(vehicles);
    }

    @Override
    public Optional<Vehicle> findById(String vehicleId) {
        return vehicles.stream().filter(vehicle -> vehicle.getId().equals(vehicleId)).findFirst();
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicles;
    }

    @Override
    public List<Vehicle> getNoRentedVehicles() {
        List<Vehicle> availableVehicles = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            if (!vehicle.isRented()) availableVehicles.add(vehicle);
        }
        return availableVehicles;
    }
}