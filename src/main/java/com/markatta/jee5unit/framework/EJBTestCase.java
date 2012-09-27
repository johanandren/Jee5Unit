package com.markatta.jee5unit.framework;

import com.markatta.jee5unit.core.Injector;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.After;

/**
 * Baseclass for tests that test an EJB. 
 *
 * @param <T> The ejb bean class
 * @author johan
 */
public abstract class EJBTestCase<T> extends EntityTestCase {

    private final Class<T> ejbClass;

    private Injector injector = new Injector();

    private T beanToTest;

   
    /**
     * The class of the ejb to test
     */
    public EJBTestCase(Class<T> ejbClass) {
        this.ejbClass = ejbClass;
    }
    
    @After 
    public final void tearDownEJBTestCase() {
        beanToTest = null;
    }
    
    

    /**
     * An instance of the bean to test with entity manager injected
     */
    protected T getBeanToTest() {
        if (beanToTest == null) {
            beanToTest = createBeanToTest();

            injector.injectField(beanToTest, Resource.class, SessionContext.class, getFakeSessionContext());
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

    private T createBeanToTest() {
        try {
            return ejbClass.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("Error creating EJB instance", ex);
        }
    }
}
