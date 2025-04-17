package org.umcs.repositories.implementation.hibernate;

import lombok.Setter;
import org.hibernate.Session;
import org.umcs.models.Vehicle;
import org.umcs.repositories.IVehicleRepository;

import java.util.List;
import java.util.Optional;

@Setter
public class HibernateVehicleRepository implements IVehicleRepository {
    private Session session;

    @Override
    public void save(Vehicle vehicle) {
        session.merge(vehicle);
    }

    @Override
    public void deleteById(String vehicleId) {
       Vehicle vehicle = session.get(Vehicle.class, vehicleId);
        if (vehicle != null) {
            session.remove(vehicle);
        }
    }

    @Override
    public Optional<Vehicle> findById(String vehicleId) {
        return Optional.ofNullable(session.get(Vehicle.class, vehicleId));
    }

    @Override
    public List<Vehicle> getNoRentedVehicles() {
        return session.createQuery("FROM Vehicle vehicle WHERE vehicle.rented = false", Vehicle.class).list();
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return session.createQuery("FROM Vehicle", Vehicle.class).list();
    }
}
