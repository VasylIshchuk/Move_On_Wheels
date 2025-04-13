package org.umcs;

import org.umcs.repositories.IRentalRepository;
import org.umcs.repositories.IUserRepository;
import org.umcs.repositories.IVehicleRepository;
import org.umcs.repositories.implementation.jdbc.JdbcRentalRepository;
import org.umcs.repositories.implementation.jdbc.JdbcUserRepository;
import org.umcs.repositories.implementation.jdbc.JdbcVehicleRepository;
import org.umcs.repositories.implementation.json.JsonRentalRepository;
import org.umcs.repositories.implementation.json.JsonUserRepository;
import org.umcs.repositories.implementation.json.JsonVehicleRepository;
import org.umcs.services.AuthenticationService;
import org.umcs.services.RentalService;
import org.umcs.services.UserService;
import org.umcs.services.VehicleService;
import org.umcs.storage.JdbcConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT NOW()");
            if (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String storageType = "jdbc";

        IUserRepository userRepository;
        IVehicleRepository vehicleRepository;
        IRentalRepository rentalRepository;

        switch (storageType) {
            case "jdbc" -> {
                userRepository = JdbcUserRepository.getInstance();
                vehicleRepository = JdbcVehicleRepository.getInstance();
                rentalRepository = JdbcRentalRepository.getInstance();
            }
            case "json" -> {
                userRepository = JsonUserRepository.getInstance();
                vehicleRepository = JsonVehicleRepository.getInstance();
                rentalRepository = JsonRentalRepository.getInstance();
            }
            default -> throw new IllegalArgumentException("Unknown storage type: " + storageType);
        }

        AuthenticationService authenticationService = new AuthenticationService(userRepository);
        VehicleService vehicleService = new VehicleService(vehicleRepository);
        RentalService rentalService = new RentalService(rentalRepository);
        UserService userService = new UserService(userRepository);


        Application.start(authenticationService, vehicleService, rentalService, userService);

    }
}


//        VehicleManager vehicleManager = VehicleManager.getInstance();
//        Rental rental = new Rental();
//
//        Car car1 = new Car("ABC123", "Toyota", "Camry", 2020, 25000);
//        Car car2 = new Car("XYZ456", "Honda", "Civic", 2021, 22000);
//        Car car3 = new Car("LMN789", "Ford", "Focus", 2022, 24000);
//
//        Motorcycle motorcycle1 = new Motorcycle("DEF321", "Yamaha", "R1", 2021, 15000, "Sport");
//        Motorcycle motorcycle2 = new Motorcycle("GHI654", "Kawasaki", "Ninja", 2020, 13000, "Sport");
//        Motorcycle motorcycle3 = new Motorcycle("JKL987", "Harley-Davidson", "Iron 883", 2022, 18000, "Cruiser");
//
//        vehicleManager.addVehicle(car1);
//        vehicleManager.addVehicle(motorcycle2);
//        vehicleManager.addVehicle(car3);
//        vehicleManager.addVehicle(motorcycle3);
//
//        rental.rentVehicle("GHI654");
//        rental.rentVehicle("GHI654");
//        rental.rentVehicle("LMN789");
//
//        vehicleManager.addVehicle(motorcycle1);
//        vehicleManager.addVehicle(car2);
//
//        rental.returnVehicle("XYZ456");
