package org.umcs.repositories.implementation.jdbc;

import org.umcs.database.DatabaseHelper;
import org.umcs.database.JdbcCreator;
import org.umcs.models.User;
import org.umcs.repositories.IUserRepository;
import org.umcs.storage.JdbcConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcUserRepository implements IUserRepository {
    private static JdbcUserRepository instance;

    public static JdbcUserRepository getInstance() {
        if (instance == null) {
            instance = new JdbcUserRepository();
        }
        return instance;
    }

    private JdbcUserRepository() {
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection()) {
            if (DatabaseHelper.validateTableExist(connection, "users")) return;
            createTableUsers(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createTableUsers(Connection connection) {
        String sql = JdbcCreator.sqlCreateTableUsers;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (id, role, login, password) VALUES (?, ?, ?, ?)";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getId());
            statement.setString(2, user.getRole());
            statement.setString(3, user.getLogin());
            statement.setString(4, user.getPassword());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        String sql = "SELECT * FROM users WHERE login = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) return Optional.empty();

            User user = JdbcCreator.createUserFromDatabase(resultSet);
            return Optional.of(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean validateUserLogin(String loginFromClient) {
        String sql = "SELECT * FROM users WHERE login = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, loginFromClient);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) return true;
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getListClients() {
        String sql = "SELECT * FROM users WHERE role = 'Client'";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<User> users = new ArrayList<>();

            while (resultSet.next()) {
                User user = JdbcCreator.createUserFromDatabase(resultSet);
                users.add(user);
            }

            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
