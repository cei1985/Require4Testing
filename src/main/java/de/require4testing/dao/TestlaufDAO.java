package de.require4testing.dao;

import java.util.List;

import de.require4testing.model.Tester;
import de.require4testing.model.Testlauf;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class TestlaufDAO {

    public void speichern(Testlauf testlauf) {
        EntityManager entityManager =
                EntityManagerProvider.createEntityManager();

        EntityTransaction transaction =
                entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.persist(testlauf);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    public List<Testlauf> findeAlle() {
        EntityManager entityManager =
                EntityManagerProvider.createEntityManager();

        try {
            return entityManager
                    .createQuery(
                            "SELECT t FROM Testlauf t "
                            + "ORDER BY t.id",
                            Testlauf.class)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    public List<Testlauf> findeAlleFuerTester(Tester tester) {
        EntityManager entityManager =
                EntityManagerProvider.createEntityManager();

        try {
            return entityManager
                    .createQuery(
                            "SELECT t FROM Testlauf t "
                            + "WHERE t.tester = :tester "
                            + "ORDER BY t.id",
                            Testlauf.class)
                    .setParameter("tester", tester)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }
}