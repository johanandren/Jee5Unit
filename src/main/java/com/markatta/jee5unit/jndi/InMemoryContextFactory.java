package com.markatta.jee5unit.jndi;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

/**
 *
 * @author johan
 */
public class InMemoryContextFactory implements InitialContextFactory {

    private static Map<String, Object> memoryContext = new HashMap();

    public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
        return new InMemoryContext(memoryContext);
    }

    public static void clear() {
        memoryContext.clear();
    }
}
