package org.umcs.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.umcs.models.Rental;
import org.umcs.models.User;
import org.umcs.models.Vehicle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class JdbcCreator {
    public static String sqlCreateTableVehicles = "CREATE TABLE vehicles " +
            "(id TEXT NOT NULL, " +
            " registration_number TEXT NOT NULL UNIQUE, " +
            " category TEXT NOT NULL, " +
            " brand TEXT NOT NULL, " +
            " model TEXT NOT NULL, " +
            " year INTEGER NOT NULL, " +
            " price DOUBLE PRECISION, " +
            " rented BOOLEAN NOT NULL, " +
            " attributes JSONB, " +
            " PRIMARY KEY ( id ))";

    public static String sqlCreateTableRentals = "CREATE TABLE rentals " +
            "(id TEXT NOT NULL, " +
            " vehicle_id TEXT NOT NULL REFERENCES vehicles (id) ON DELETE CASCADE, " +
            " user_id TEXT NOT NULL REFERENCES users (id) ON DELETE CASCADE, " +
            " rent_date DATE NOT NULL, " +
            " return_date DATE NOT NULL, " +
            " PRIMARY KEY ( id ))";

    public static String sqlCreateTableUsers = "CREATE TABLE users " +
            "(id TEXT NOT NULL, " +
            " role TEXT, " +
            " CHECK (role IN ('Admin','Client')), " +
            " login TEXT NOT NULL UNIQUE, " +
            " password TEXT NOT NULL, " +
            " PRIMARY KEY ( id ))";

    public static User createUserFromDatabase(ResultSet sqlResultSet) {
        try {
            return User.builder()
                    .id(sqlResultSet.getString("id"))
                    .role(sqlResultSet.getString("role"))
                    .login(sqlResultSet.getString("login"))
                    .password(sqlResultSet.getString("password"))
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Rental createRentalFromDatabase(ResultSet sqlResultSet) {
        try {
            return Rental.builder()
                    .id(sqlResultSet.getString("id"))
                    .vehicleId(sqlResultSet.getString("vehicle_id"))
                    .userId(sqlResultSet.getString("user_id"))
                    .rentDate(sqlResultSet.getString("rent_date"))
                    .returnDate(sqlResultSet.getString("return_date"))
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Vehicle createVehicleFromDatabase(ResultSet sqlResultSet) {
        Map<String, Object> attributes = getAttributesFromDatabase(sqlResultSet);
        try {
            return Vehicle.builder()
                    .id(sqlResultSet.getString("id"))
                    .registrationNumber(sqlResultSet.getString("registration_number"))
                    .category(sqlResultSet.getString("category"))
                    .brand(sqlResultSet.getString("brand"))
                    .model(sqlResultSet.getString("model"))
                    .year(sqlResultSet.getInt("year"))
                    .price(sqlResultSet.getDouble("price"))
                    .attributes(attributes != null ? attributes : new HashMap<>())
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, Object> getAttributesFromDatabase(ResultSet resultSet) {
        try {
            String jsonbAttributes = resultSet.getString("attributes");
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonbAttributes, new TypeReference<>() {
            });
        } catch (SQLException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
