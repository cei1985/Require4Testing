package de.require4testing.dao;

import java.util.List;

import de.require4testing.model.Testfall;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class TestfallDAO {

    public void speichern(Testfall testfall) {
        EntityManager entityManager = EntityManagerProvider.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.persist(testfall);
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

    public List<Testfall> findeAlleFuerAnforderung(Long anforderungsId) {
        EntityManager entityManager = EntityManagerProvider.createEntityManager();

        try {
            return entityManager
                    .createQuery(
                            "SELECT t FROM Testfall t "
                            + "WHERE t.anforderung.id = :anforderungsId "
                            + "ORDER BY t.id",
                            Testfall.class)
                    .setParameter("anforderungsId", anforderungsId)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }
}