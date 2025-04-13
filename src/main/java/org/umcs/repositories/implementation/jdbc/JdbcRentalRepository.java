package org.umcs.repositories.implementation.jdbc;

import org.umcs.database.DatabaseHelper;
import org.umcs.database.JdbcCreator;
import org.umcs.models.Rental;
import org.umcs.repositories.IRentalRepository;
import org.umcs.storage.JdbcConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcRentalRepository implements IRentalRepository {
    private static JdbcRentalRepository instance;

    public static JdbcRentalRepository getInstance() {
        if (instance == null) {
            instance = new JdbcRentalRepository();
        }
        return instance;
    }

    private JdbcRentalRepository() {
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection()) {
            if (DatabaseHelper.validateTableExist(connection, "rentals")) return;
            createTableRentals(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createTableRentals(Connection connection) {
        String sql = JdbcCreator.sqlCreateTableRentals;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Rental rentalVehicle) {
        String sql = "INSERT INTO rentals (id, vehicle_id, user_id, rent_date, return_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, rentalVehicle.getId());
            statement.setString(2, rentalVehicle.getVehicleId());
            statement.setString(3, rentalVehicle.getUserId());
            statement.setDate(4, Date.valueOf(rentalVehicle.getRentDate()));
            statement.setDate(5, Date.valueOf(rentalVehicle.getReturnDate()));

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeRentalById(String rentalId) {
        String sql = "DELETE FROM rentals WHERE id = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, rentalId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Rental> findByUserId(String userId) {
        String sql = "SELECT * FROM rentals WHERE user_id = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) return Optional.empty();

            Rental rental = JdbcCreator.createRentalFromDatabase(resultSet);
            return Optional.of(rental);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Rental> getListRentals() {
        String sql = "SELECT * FROM rentals";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<Rental> rentals = new ArrayList<>();

            while (resultSet.next()) {
                Rental rental = JdbcCreator.createRentalFromDatabase(resultSet);
                rentals.add(rental);
            }

            return rentals;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
