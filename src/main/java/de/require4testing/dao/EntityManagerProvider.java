package de.require4testing.dao;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerProvider {

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY =
            createEntityManagerFactory();

    private EntityManagerProvider() {
    }

    private static EntityManagerFactory createEntityManagerFactory() {
        String dbPassword = System.getenv("DB_PASSWORD");

        if (dbPassword == null || dbPassword.isBlank()) {
            throw new IllegalStateException(
                    "Die Umgebungsvariable DB_PASSWORD ist nicht gesetzt.");
        }

        Map<String, Object> properties = new HashMap<>();
        properties.put("jakarta.persistence.jdbc.password", dbPassword);

        return Persistence.createEntityManagerFactory(
                "Require4TestingPU",
                properties);
    }

    public static EntityManager createEntityManager() {
        return ENTITY_MANAGER_FACTORY.createEntityManager();
    }
}