package de.require4testing.dao;

import java.util.List;

import de.require4testing.model.Testdurchfuehrung;
import de.require4testing.model.Tester;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class TestdurchfuehrungDAO {

    public void speichern(
            Testdurchfuehrung testdurchfuehrung) {

        EntityManager entityManager =
                EntityManagerProvider.createEntityManager();

        EntityTransaction transaction =
                entityManager.getTransaction();

        try {

            transaction.begin();

            entityManager.persist(
                    testdurchfuehrung);

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

    public List<Testdurchfuehrung> findeFuerTester(
            Tester tester) {

        EntityManager entityManager =
                EntityManagerProvider.createEntityManager();

        try {

            return entityManager
                    .createQuery(
                            "SELECT td "
                            + "FROM Testdurchfuehrung td "
                            + "WHERE td.testlauf.tester = :tester "
                            + "ORDER BY td.id",
                            Testdurchfuehrung.class)
                    .setParameter(
                            "tester",
                            tester)
                    .getResultList();

        } finally {

            entityManager.close();
        }
    }

    public List<Testdurchfuehrung> findeFuerTestlauf(
            Long testlaufId) {

        EntityManager entityManager =
                EntityManagerProvider.createEntityManager();

        try {

            return entityManager
                    .createQuery(
                            "SELECT td "
                            + "FROM Testdurchfuehrung td "
                            + "WHERE td.testlauf.id = :testlaufId "
                            + "ORDER BY td.id",
                            Testdurchfuehrung.class)
                    .setParameter(
                            "testlaufId",
                            testlaufId)
                    .getResultList();

        } finally {

            entityManager.close();
        }
    }

    public void aktualisieren(
            Testdurchfuehrung testdurchfuehrung) {

        EntityManager entityManager =
                EntityManagerProvider.createEntityManager();

        EntityTransaction transaction =
                entityManager.getTransaction();

        try {

            transaction.begin();

            entityManager.merge(
                    testdurchfuehrung);

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
}