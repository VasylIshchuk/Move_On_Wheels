package org.umcs.repositories.implementation.json;

import com.google.gson.reflect.TypeToken;
import org.umcs.storage.JsonFileStorage;
import org.umcs.models.Rental;
import org.umcs.repositories.IRentalRepository;

import java.util.List;
import java.util.Optional;

public class JsonRentalRepository implements IRentalRepository {
    private static JsonRentalRepository instance;

    private final List<Rental> rentals;
    private final String filePath = "rentals.json";
    private final JsonFileStorage<Rental> jsonFileStorage = new JsonFileStorage<>(filePath);

    private JsonRentalRepository() {
        this.rentals = jsonFileStorage.load(new TypeToken<List<Rental>>() {
        }.getType());
    }

    public static JsonRentalRepository getInstance() {
        if (instance == null) {
            instance = new JsonRentalRepository();
        }
        return instance;
    }

    @Override
    public void save(Rental rental) {
        rentals.add(rental);
        jsonFileStorage.saveToFile(rentals);
    }

    @Override
    public void removeRentalById(String rentalId){
        rentals.removeIf(rental -> rental.getId().equals(rentalId));
        jsonFileStorage.saveToFile(rentals);
    }

    @Override
    public Optional<Rental> findByUserId(String userId) {
        return rentals.stream().filter(rental -> rental.getUserId().equals(userId)).findFirst();
    }

    @Override
    public List<Rental> getListRentals() {
        return rentals;
    }
}
