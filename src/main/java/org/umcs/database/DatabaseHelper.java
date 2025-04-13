package org.umcs.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHelper {
    public static boolean validateTableExist(Connection connection, String tableName) {
        String sql = "SELECT EXISTS ( " +
                " SELECT 1 FROM information_schema.tables " +
                " WHERE table_schema = 'public' AND table_name = ? " +
                ") AS table_existence";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1,tableName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) return resultSet.getBoolean("table_existence");
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
