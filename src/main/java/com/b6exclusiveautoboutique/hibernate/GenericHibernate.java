package com.b6exclusiveautoboutique.hibernate;

import com.b6exclusiveautoboutique.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GenericHibernate {
    protected EntityManagerFactory entityManagerFactory;

    protected EntityManager entityManager;

    public GenericHibernate(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public <T> void create(T entity) {
        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public <T> void update(T entity) {
        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public <T> void delete(Class<T> entityClass, int id) {
        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();
            T object = entityManager.find(entityClass, id);
            entityManager.remove(object);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public <T> T getEntityById(Class<T> entityClass, int id) {
        T result = null;
        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();
            result = entityManager.find(entityClass, id);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
        return result;
    }

    public <T> List<T> getAllRecords(Class<T> entityClass) {

        List<T> result = new ArrayList<>();
        try {
            entityManager = getEntityManager();
            CriteriaQuery query = entityManager.getCriteriaBuilder().createQuery();
            query.select(query.from(entityClass));
            Query q = entityManager.createQuery(query);
            result = q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
        return result;
    }

}
