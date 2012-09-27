package com.markatta.jee5unit.framework;

import com.markatta.jee5unit.core.HibernateConfiguration;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * Base test case for tests that needs an entity manager to persist and test JPA
 * stuff with entities. The entity manager is kept across tests to avoid having 
 * the schema generated once per test so you must make sure to rollback each test
 * transaction or remove the entities created at the end of each test case.
 *
 * @author johan
 */
public abstract class EntityTestCase extends HasGotJeeContext implements EntityTransactional {

    private EntityManager entityManager;

   

    /**
     * The first time in a test this will create a new entity manager and all
     * forthcoming invokations in the same TestCase will return the same
     * instance.
     */
    protected final EntityManager getEntityManager() {
        if (entityManager == null) {
            // once per test but not until entity manager is used
            entityManager = HibernateConfiguration.getInstance().createEntityManager();
        }
        return entityManager;
    }

    public final EntityTransaction getTransaction() {
        return getEntityManager().getTransaction();
    }
}
