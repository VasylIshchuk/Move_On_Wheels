package org.umcs.storage;

import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import org.umcs.models.Rental;
import org.umcs.models.User;
import org.umcs.models.Vehicle;

public class HibernateConfig {
    @Getter
    private static final SessionFactory sessionFactory;
    static {
        try {
            Configuration configuration = new Configuration();
            configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
            configuration.setProperty("hibernate.connection.url", JdbcConnectionManager.getDatabaseUrl());
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect"); configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.format_sql", "true");
            configuration.setProperty("hibernate.hbm2ddl.auto", "update");
            configuration.registerTypeOverride(new JsonBinaryType(), new String[]{"jsonb"});
            configuration.addAnnotatedClass(Vehicle.class);
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Rental.class);
            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError("Initialize Hibernate ERROR: " + ex);
        }
    }
}