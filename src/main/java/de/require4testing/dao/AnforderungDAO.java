package de.require4testing.dao;

import java.util.List;

import de.require4testing.model.Anforderung;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class AnforderungDAO {

    public void speichern(Anforderung anforderung) {
        EntityManager entityManager = EntityManagerProvider.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.persist(anforderung);
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

    public List<Anforderung> findeAlle() {
        EntityManager entityManager = EntityManagerProvider.createEntityManager();

        try {
            return entityManager
                    .createQuery("SELECT a FROM Anforderung a ORDER BY a.id", Anforderung.class)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }
}