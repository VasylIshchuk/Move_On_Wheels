package org.umcs.services.implementation.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.umcs.Creator;
import org.umcs.MenuManager;
import org.umcs.models.Vehicle;
import org.umcs.repositories.implementation.hibernate.HibernateVehicleRepository;
import org.umcs.services.IVehicleService;
import org.umcs.storage.HibernateConfig;

import java.util.List;

public class VehicleHibernateService implements IVehicleService {
    private final HibernateVehicleRepository vehicleRepository;

    public VehicleHibernateService(HibernateVehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public void showListVehicles() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            vehicleRepository.setSession(session);
            List<Vehicle> listVehicles = vehicleRepository.getAllVehicles();
            MenuManager.showList(listVehicles);
        }
    }

    @Override
    public void addVehicle() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            vehicleRepository.setSession(session);

            Vehicle vehicle = Creator.createVehicleFromConsole();
            vehicleRepository.save(vehicle);

            transaction.commit();
        }
    }

    @Override
    public void removeVehicle() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            vehicleRepository.setSession(session);

            Vehicle vehicle = MenuManager.getAvailableVehicleIdFromConsole(vehicleRepository);
            vehicleRepository.deleteById(vehicle.getId());

            transaction.commit();
        }
    }
}
