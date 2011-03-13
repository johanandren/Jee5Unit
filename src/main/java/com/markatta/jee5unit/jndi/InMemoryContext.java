package com.markatta.jee5unit.jndi;

import java.util.Hashtable;
import java.util.Map;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 *
 * @author johan
 */
public class InMemoryContext implements Context {

    private Map<String, Object> memoryContext;

    public InMemoryContext(Map<String, Object> memoryContext) {
        this.memoryContext = memoryContext;
    }

    public Object lookup(Name name) throws NamingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object lookup(String name) throws NamingException {
        if (!memoryContext.containsKey(name)) {
            throw new NamingException("Nothing in the jndi context for the name " + name);
        }

        return memoryContext.get(name);

    }

    public void bind(Name name, Object obj) throws NamingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void bind(String name, Object obj) throws NamingException {
        memoryContext.put(name, obj);
    }

    public void rebind(Name name, Object obj) throws NamingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void rebind(String name, Object obj) throws NamingException {
        memoryContext.put(name, obj);
    }

    public void unbind(Name name) throws NamingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void unbind(String name) throws NamingException {
        memoryContext.remove(name);
    }

    public void rename(Name oldName, Name newName) throws NamingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void rename(String oldName, String newName) throws NamingException {
        Object object = lookup(oldName);
        unbind(oldName);
        bind(newName, object);
    }

    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public NamingEnumeration<NameClassPair> list(String name) throws NamingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public NamingEnumeration<Binding> listBindings(String name) throws NamingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void destroySubcontext(Name name) throws NamingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void destroySubcontext(String name) throws NamingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Context createSubcontext(Name name) throws NamingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Context createSubcontext(String name) throws NamingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object lookupLink(Name name) throws NamingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object lookupLink(String name) throws NamingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public NameParser getNameParser(Name name) throws NamingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public NameParser getNameParser(String name) throws NamingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Name composeName(Name name, Name prefix) throws NamingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String composeName(String name, String prefix) throws NamingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object addToEnvironment(String propName, Object propVal) throws NamingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object removeFromEnvironment(String propName) throws NamingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Hashtable<?, ?> getEnvironment() throws NamingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void close() throws NamingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getNameInNamespace() throws NamingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
