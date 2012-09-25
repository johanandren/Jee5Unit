package com.markatta.jee5unit.framework;

import com.markatta.jee5unit.core.Injector;
import com.markatta.jee5unit.ejb.FakedSessionContext;
import java.security.Principal;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * <p> Baseclass for tests that test an EJB. Note that the EJB instance will
 * keep its state throuhout the test so any injected field such as
 * EntityManager, SessionContext or other EJBs will not be automatically reset
 * between tests. </p> <p> The injected EJB:s can be reset with the
 * {@link #clearInjectedEJBs()} </p>
 *
 * @param <T> The ejb bean class
 * @author johan
 */
public abstract class EJBTestCase<T> extends EntityTestCase {

    private Class<T> ejbClass;

    private Injector injector = new Injector();

    private T beanToTest;

    private FakedSessionContext sessionContext = new FakedSessionContext();

    public EJBTestCase(Class<T> ejbClass) {
        super();
        this.ejbClass = ejbClass;
    }

    /**
     * An instance of the bean to test with entity manager injected
     */
    protected T getBeanToTest() {
        if (beanToTest == null) {
            beanToTest = createBeanToTest();

            injector.injectField(beanToTest, Resource.class, SessionContext.class, sessionContext);
        }

        // Avoid creating an entity manager if possible, if a project has only testcases for
        // EJB:s this will avoid scanning all entities
        if (injector.containsAnnotation(beanToTest.getClass(), PersistenceContext.class)) {
            EntityManager em = getEntityManager();

            injector.injectField(beanToTest, PersistenceContext.class, EntityManager.class, em);
        }

        return beanToTest;
    }
    
    
    /**
     * Inject anything into the beanToTest
     * @param <A> The type of the injected resource
     * @param annotation The annotation to look for
     * @param instance An instance to inject
     */
    protected <A> void inject(Class<?> annotation, Class<A> fieldType, A instance) {
        getBeanToTest();
        injector.injectField(beanToTest, annotation, fieldType, instance);
    }

    /**
     * Injects the given bean into
     * <code>beanToTest</code>.
     *
     * @param ejbInterfaceUsed The local or remote interface class
     * @param bean A mocked instance of the bean for that EJB
     */
    protected <A> void injectEJB(Class<A> ejbInterfaceUsed, A bean) {
        // make sure it is initialized
        getBeanToTest();
        injector.injectField(beanToTest, EJB.class, ejbInterfaceUsed, bean);
    }

    /**
     * Set any field with
     *
     * @EJB annotations to <code>null</code>. Call this in the end of your test
     * to avoid re-use of mocked EJBs across test methods.
     */
    protected void clearInjectedEJBs() {
        if (beanToTest != null) {
            injector.injectField(beanToTest, EJB.class, null, null);
        }
    }

    /**
     * This will set the caller principal of the session context of the bean.
     */
    protected void setCallerPrincipal(Principal principal) {
        sessionContext.setPrincipal(principal);
    }

    /**
     * @return A fake session context that will be injected into the EJB before
     * returning it from getBeanToTest,
     */
    protected SessionContext getFakeSessionContext() {

        return sessionContext;
    }

    private T createBeanToTest() {
        try {
            return ejbClass.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("Error creating EJB instance", ex);
        }
    }
}
