package de.require4testing.dao;

import java.util.List;

import de.require4testing.model.Tester;
import jakarta.persistence.EntityManager;

public class TesterDAO {

    public List<Tester> findeAlle() {
        EntityManager entityManager = EntityManagerProvider.createEntityManager();

        try {
            return entityManager
                    .createQuery(
                            "SELECT t FROM Tester t ORDER BY t.id",
                            Tester.class)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }
}