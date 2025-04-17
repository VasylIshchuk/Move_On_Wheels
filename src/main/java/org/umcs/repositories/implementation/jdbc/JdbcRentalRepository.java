package org.umcs.repositories.implementation.jdbc;

import org.umcs.database.JdbcHelper;
import org.umcs.database.JdbcCreator;
import org.umcs.models.Rental;
import org.umcs.models.Vehicle;
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
            if (JdbcHelper.validateTableExist(connection, "rentals")) return;
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
    public void save(Rental rental) {
        Optional<Rental> existingVehicle = findById(rental.getId());

        if (existingVehicle.isPresent()){
            update(rental);
        }else{
            addNewRental(rental);
        }
    }

    private void update(Rental rental) {
        String sql = "UPDATE rentals SET rent_date = ?, return_date = ? WHERE id = ?";

        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setTimestamp(1, Timestamp.valueOf(rental.getRentDate()));

            if (rental.getReturnDate() != null) {
                statement.setTimestamp(2, Timestamp.valueOf(rental.getReturnDate()));
            } else {
                statement.setNull(2, java.sql.Types.TIMESTAMP);
            }

            statement.setString(3, rental.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private void addNewRental(Rental rental){
        String sql = "INSERT INTO rentals (id, vehicle_id, user_id, rent_date, return_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, rental.getId());
            statement.setString(2, rental.getVehicleId());
            statement.setString(3, rental.getUserId());
            statement.setTimestamp(4, Timestamp.valueOf(rental.getRentDate()));

            if (rental.getReturnDate() != null) {
                statement.setTimestamp(5, Timestamp.valueOf(rental.getReturnDate()));
            } else {
                statement.setNull(5, java.sql.Types.TIMESTAMP);
            }

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
    public Optional<Rental> findById(String id) {
        String sql = "SELECT * FROM rentals WHERE id = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) return Optional.empty();

            Rental rental = JdbcCreator.createRentalFromDatabase(resultSet);
            return Optional.of(rental);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Rental> findByUserIdAndReturnDateIsNull(String userId) {
        String sql = "SELECT * FROM rentals WHERE user_id = ? AND return_date IS NULL";
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
    public List<Rental> getAllRentals() {
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
