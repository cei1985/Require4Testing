package de.require4testing.dao;

import java.util.List;

import de.require4testing.model.Tester;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class TesterDAO {

    public void speichern(Tester tester) {
        EntityManager entityManager = EntityManagerProvider.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.persist(tester);
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