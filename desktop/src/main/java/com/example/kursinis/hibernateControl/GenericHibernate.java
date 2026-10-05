package com.example.kursinis.hibernateControl;

import com.example.kursinis.model.Review;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.ArrayList;
import java.util.List;

public class GenericHibernate {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("kursinis");

    // -------------------- CREATE --------------------
    public <T> void create(T entity) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally { em.close(); }
    }

    // -------------------- UPDATE --------------------
    public <T> void update(T entity) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally { em.close(); }
    }

    // --------------------- DELETE ---------------------
    public <T> void delete(Class<T> cls, Object id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            T entity = em.find(cls, id);
            if (entity != null) em.remove(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally { em.close(); }
    }

    // -------------------- GET ALL --------------------
    public <T> List<T> getAllRecords(Class<T> entityClass) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT t FROM " + entityClass.getSimpleName() + " t",
                    entityClass
            ).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally { em.close(); }
    }

    // -------------------- GET BY FIELD --------------------
    public <T> T getByField(Class<T> type, String field, Object value) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT t FROM " + type.getSimpleName() + " t WHERE t." + field + " = :val";

            List<T> list = em.createQuery(jpql, type)
                    .setParameter("val", value)
                    .setMaxResults(1)
                    .getResultList();

            return list.isEmpty() ? null : list.get(0);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally { em.close(); }
    }

    // -------------------- FIND BY TWO FIELDS --------------------
    public <T> T findByTwoFields(Class<T> type, String f1, Object v1, String f2, Object v2) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql =
                    "SELECT t FROM " + type.getSimpleName() + " t " +
                            "WHERE t." + f1 + " = :v1 AND t." + f2 + " = :v2";

            List<T> list = em.createQuery(jpql, type)
                    .setParameter("v1", v1)
                    .setParameter("v2", v2)
                    .setMaxResults(1)
                    .getResultList();

            return list.isEmpty() ? null : list.get(0);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally { em.close(); }
    }

    // -------------------- CHAT MESSAGES --------------------
    public List<Review> getChatMessages(int chatId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT r FROM Review r WHERE r.chat.id = :id ORDER BY r.dateCreated",
                            Review.class
                    ).setParameter("id", chatId)
                    .getResultList();

        } finally { em.close(); }
    }

    // -------------------- SAVE CHAT MESSAGE --------------------
    public void saveChatMessage(Review review, int chatId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            review.setChat(em.find(review.getChat().getClass(), chatId));

            em.persist(review);

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally { em.close(); }
    }
}
