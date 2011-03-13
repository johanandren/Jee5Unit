package com.markatta.jee5unit.framework;

import com.markatta.jee5unit.core.HibernateConfiguration;
import com.markatta.jee5unit.core.Jee5UnitRuntimeException;
import com.markatta.jee5unit.jndi.InMemoryContextFactory;
import com.markatta.jee5unit.runners.UsesJNDI;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.junit.After;
import org.junit.Before;

/**
 * Base test case for tests that needs an entity manager to persist and test
 * JPA stuff with entities.
 * 
 * @author johan
 */
public abstract class EntityTestCase implements EntityTransactional {

    private EntityManager entityManager;

    private boolean usesJNDI;

    public EntityTestCase() {
        usesJNDI = getClass().isAnnotationPresent(UsesJNDI.class);
        if (usesJNDI) {


            System.setProperty("java.naming.factory.initial", "com.markatta.jee5unit.jndi.InMemoryContextFactory");
        }
    }

    @Before
    public void setupJNDIContext() {
        if (usesJNDI) {
            UsesJNDI jndiAnnotation = getClass().getAnnotation(UsesJNDI.class);
            // if the test wants the entity manager available from jndi
            // publish it
            if (jndiAnnotation.publishEntityManager()) {
                String publishAs = jndiAnnotation.entityManagerName();

                try {
                    InitialContext context = new InitialContext();
                    context.bind(publishAs, getEntityManager());

                } catch (NamingException ex) {
                    throw new Jee5UnitRuntimeException("Error publishing entity manager as " + publishAs, ex);
                }
            }
        }
    }

    @After
    public void clearJNDIContext() {
        if (usesJNDI) {
            InMemoryContextFactory.clear();
        }
    }

    protected void publishOnJNDI(String name, Object object) {
        if (!usesJNDI) {
            throw new Jee5UnitRuntimeException("Tried to publish something on JNDI but no context was setup. " +
                    "You need to annotate your testcase with " + UsesJNDI.class.getSimpleName() + " to use JNDI.", null);
        }
        try {
            InitialContext context = new InitialContext();
            context.bind(name, object);
        } catch (NamingException ex) {
            throw new Jee5UnitRuntimeException("Tried to publish " + object + " at JNDI name " + name + " but failed", ex);
        }
    }

    /**
     * The first time in a test this will create a new entity manager and
     * all forthcoming invokations in the same TestCase will return the same
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
