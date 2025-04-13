package org.umcs.repositories.implementation.jdbc;

import com.google.gson.Gson;
import org.umcs.database.DatabaseHelper;
import org.umcs.database.JdbcCreator;
import org.umcs.models.Vehicle;
import org.umcs.repositories.IVehicleRepository;
import org.umcs.storage.JdbcConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JdbcVehicleRepository implements IVehicleRepository {
    private static JdbcVehicleRepository instance;

    public static JdbcVehicleRepository getInstance() {
        if (instance == null) {
            instance = new JdbcVehicleRepository();
        }
        return instance;
    }

    private JdbcVehicleRepository() {
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection()) {
            if (DatabaseHelper.validateTableExist(connection, "vehicles")) return;
            createTableVehicles(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createTableVehicles(Connection connection) {
        String sql = JdbcCreator.sqlCreateTableVehicles;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(String vehicleId) {
        String sql = "DELETE FROM vehicles WHERE id = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, vehicleId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Vehicle vehicle) {
        Optional<Vehicle> vehicle1 = findById(vehicle.getId());
        if (vehicle1.isPresent()){
            update(vehicle);
        }else{
            addNewVehicle(vehicle);
        }
    }

    private void update(Vehicle vehicle) {
        String sql = "UPDATE vehicles SET registration_number = ?, category = ?, brand = ?, model = ?, year = ?, price = ?, rented = ?, attributes = ?::jsonb WHERE id = ?";
        Gson gson = new Gson();

        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, vehicle.getRegistrationNumber());
            statement.setString(2, vehicle.getCategory());
            statement.setString(3, vehicle.getBrand());
            statement.setString(4, vehicle.getModel());
            statement.setInt(5, vehicle.getYear());
            statement.setDouble(6, vehicle.getPrice());
            statement.setBoolean(7, vehicle.isRented());
            statement.setString(8, gson.toJson(vehicle.getAttributes()));
            statement.setString(9, vehicle.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addNewVehicle(Vehicle vehicle){
        String sql = "INSERT INTO vehicles (id, registration_number, category, brand, model, year, price, rented, attributes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?::jsonb)";
        Gson gson = new Gson();

        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, vehicle.getId());
            statement.setString(2, vehicle.getRegistrationNumber());
            statement.setString(3, vehicle.getCategory());
            statement.setString(4, vehicle.getBrand());
            statement.setString(5, vehicle.getModel());
            statement.setInt(6, vehicle.getYear());
            statement.setDouble(7, vehicle.getPrice());
            statement.setBoolean(8, vehicle.isRented());
            statement.setString(9, gson.toJson(vehicle.getAttributes()));

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Vehicle> findById(String vehicleId) {
        String sql = "SELECT * FROM vehicles WHERE id = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, vehicleId);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) return Optional.empty();

            Vehicle vehicle = JdbcCreator.createVehicleFromDatabase(resultSet);
            return Optional.of(vehicle);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        String sql = "SELECT * FROM vehicles";
        return fetchVehiclesByQuery(sql);
    }

    @Override
    public List<Vehicle> getNoRentedVehicles() {
        String sql = "SELECT * FROM vehicles WHERE rented = FALSE";
        return fetchVehiclesByQuery(sql);
    }

    private List<Vehicle> fetchVehiclesByQuery(String sql) {
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<Vehicle> vehicles = new ArrayList<>();

            while (resultSet.next()) {
                Vehicle vehicle = JdbcCreator.createVehicleFromDatabase(resultSet);
                vehicles.add(vehicle);
            }

            return vehicles;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
