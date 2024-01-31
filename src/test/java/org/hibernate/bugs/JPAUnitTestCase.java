package org.hibernate.bugs;

import java.util.List;
import java.util.Set;

import org.hibernate.entity.TestEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

    private EntityManagerFactory entityManagerFactory;

    @Before
    public void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("templatePU");
    }

    @After
    public void destroy() {
        entityManagerFactory.close();
    }

    // Entities are auto-discovered, so just add them anywhere on class-path
    // Add your tests, using standard JUnit.
    @Test
    public void hhh17693Test() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        final TestEntity entity = new TestEntity();
        entity.descriptions = Set.of("P_1", "P_2", "P_3");
        entityManager.persist(entity);

        final List<TestEntity> results = entityManager.createQuery(
                        "select e from TestEntity e where e.descriptions like :text",
                        TestEntity.class
                )
                .setParameter("text", "%,P_2,%")
                .getResultList();

        Assertions.assertEquals(1, results.size());

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
