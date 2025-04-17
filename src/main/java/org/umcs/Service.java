package org.umcs;

import org.umcs.repositories.IRentalRepository;
import org.umcs.repositories.IUserRepository;
import org.umcs.repositories.IVehicleRepository;
import org.umcs.repositories.implementation.hibernate.HibernateRentalRepository;
import org.umcs.repositories.implementation.hibernate.HibernateUserRepository;
import org.umcs.repositories.implementation.hibernate.HibernateVehicleRepository;
import org.umcs.repositories.implementation.jdbc.JdbcRentalRepository;
import org.umcs.repositories.implementation.jdbc.JdbcUserRepository;
import org.umcs.repositories.implementation.jdbc.JdbcVehicleRepository;
import org.umcs.repositories.implementation.json.JsonRentalRepository;
import org.umcs.repositories.implementation.json.JsonUserRepository;
import org.umcs.repositories.implementation.json.JsonVehicleRepository;
import org.umcs.services.IAuthenticationService;
import org.umcs.services.IRentalService;
import org.umcs.services.IUserService;
import org.umcs.services.IVehicleService;
import org.umcs.services.implementation.hibernate.AuthenticationHibernateService;
import org.umcs.services.implementation.hibernate.RentalHibernateService;
import org.umcs.services.implementation.hibernate.UserHibernateService;
import org.umcs.services.implementation.hibernate.VehicleHibernateService;
import org.umcs.services.implementation.standard.AuthenticationService;
import org.umcs.services.implementation.standard.RentalService;
import org.umcs.services.implementation.standard.UserService;
import org.umcs.services.implementation.standard.VehicleService;


public class Service {
    public static IAuthenticationService authenticationService;
    public static IVehicleService vehicleService;
    public static IRentalService rentalService;
    public static IUserService userService;

    public static void createServices() {
        MenuManager.showStorageOptions();
        String input = MenuManager.getNumberFromConsole();

        String storageType = switch (input) {
            case "1" -> "hibernate";
            case "2" -> "jdbc";
            case "3" -> "json";
            default -> throw new IllegalArgumentException("Invalid input: " + input);
        };

        switch (storageType) {
            case "hibernate" -> setupHibernate();
            case "jdbc" -> setupJdbc();
            case "json" -> setupJson();
        }
    }

    private static void setupHibernate() {
        HibernateUserRepository userRepo = new HibernateUserRepository();
        HibernateVehicleRepository vehicleRepo = new HibernateVehicleRepository();
        HibernateRentalRepository rentalRepo = new HibernateRentalRepository();

        authenticationService = new AuthenticationHibernateService(userRepo);
        vehicleService = new VehicleHibernateService(vehicleRepo);
        rentalService = new RentalHibernateService(vehicleRepo, rentalRepo);
        userService = new UserHibernateService(userRepo);
    }

    private static void setupJdbc() {
        IUserRepository userRepo = JdbcUserRepository.getInstance();
        IVehicleRepository vehicleRepo = JdbcVehicleRepository.getInstance();
        IRentalRepository rentalRepo = JdbcRentalRepository.getInstance();

        setUpStandardServices(userRepo, vehicleRepo, rentalRepo);
    }

    private static void setupJson() {
        IUserRepository userRepo = JsonUserRepository.getInstance();
        IVehicleRepository vehicleRepo = JsonVehicleRepository.getInstance();
        IRentalRepository rentalRepo = JsonRentalRepository.getInstance();

        setUpStandardServices(userRepo, vehicleRepo, rentalRepo);
    }

    private static void setUpStandardServices(IUserRepository userRepo, IVehicleRepository vehicleRepo, IRentalRepository rentalRepo) {
        authenticationService = new AuthenticationService(userRepo);
        vehicleService = new VehicleService(vehicleRepo);
        rentalService = new RentalService(vehicleRepo, rentalRepo);
        userService = new UserService(userRepo);
    }
}