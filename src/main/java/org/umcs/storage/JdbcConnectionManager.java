package org.umcs.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcConnectionManager {
    private final String database_url;
    private static JdbcConnectionManager INSTANCE;

    public static JdbcConnectionManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new JdbcConnectionManager();
        }
        return INSTANCE;
    }

    private JdbcConnectionManager() {
        database_url = getDatabaseUrl();
    }

    public static String getDatabaseUrl() {
        try {
            Path path = Paths.get("src/main/resources/config.properties");

            if (!Files.exists(path)) {
                throw new RuntimeException("The \"config.properties\" file was not found!");
            }

            try (InputStream input = Files.newInputStream(path)) {
                Properties props = new Properties();
                props.load(input);
                String databaseUrl = props.getProperty("DB_URL");

                if (databaseUrl == null) {
                    throw new RuntimeException("The \"DB_URL\" value was not found in \"config.properties\"!");
                }

                return databaseUrl;
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while loading \"config.properties\"", e);
        }
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(database_url);
        } catch (SQLException e) {
            throw new RuntimeException("Connection Failed!", e);
        }
    }
}
