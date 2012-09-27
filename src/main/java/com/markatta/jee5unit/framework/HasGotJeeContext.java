package com.markatta.jee5unit.framework;

import com.markatta.jee5unit.core.Jee5UnitRuntimeException;
import com.markatta.jee5unit.ejb.FakedSessionContext;
import com.markatta.jee5unit.jndi.InMemoryContextFactory;
import java.security.Principal;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.Before;

/**
 * Provides a fake SessionContext and the possiblity to publish and lookup stuff
 * on a small fake jndi. Note that the jndi store is static so this makes it
 * impossible to run tests in parallell.
 *
 * The session context and jndi will be replaced before each test is run.
 *
 * @author Johan Andren <johan.andren@mejsla.se>
 */
public abstract class HasGotJeeContext {

    private FakedSessionContext sessionContext;

    private InitialContext initialContext;

    protected void publishOnJNDI(String name, Object object) {
        try {
            initialContext.bind(name, object);
        } catch (NamingException ex) {
            throw new Jee5UnitRuntimeException("Tried to publish " + object + " at JNDI name " + name + " but failed", ex);
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
    protected FakedSessionContext getFakeSessionContext() {
        return sessionContext;
    }

    @Before
    public final void setupJeeContext() {
        try {
            System.setProperty("java.naming.factory.initial", "com.markatta.jee5unit.jndi.InMemoryContextFactory");
            initialContext = new InitialContext();
        } catch (NamingException ex) {
            throw new Jee5UnitRuntimeException("Failed to create initial context", ex);
        }

        sessionContext = new FakedSessionContext();
        publishOnJNDI("java:comp/EJBContext", sessionContext);
        publishOnJNDI("java:comp/env/sessionContext", sessionContext);
    }

    @After
    public final void tearDownJeeContext() {
        sessionContext = null;
        InMemoryContextFactory.clear();
        System.clearProperty("java.naming.factory.initial");
    }
}
