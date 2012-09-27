package com.markatta.jee5unit.ejb;

import java.lang.String;
import java.security.Identity;
import java.security.Principal;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.SessionContext;
import javax.ejb.TimerService;
import javax.transaction.UserTransaction;
import javax.xml.rpc.handler.MessageContext;

/**
 * TODO inject this into tested beans
 *
 * For now this is just an adapter to make it easy to fake the session context
 * of your bean.
 *
 * @author johan
 */
public class FakedSessionContext implements SessionContext {

    private Principal principal;

    private final Set<String> roles = new HashSet<String>();

    public EJBLocalObject getEJBLocalObject() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported.");
    }

    public EJBObject getEJBObject() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported.");
    }

    public MessageContext getMessageContext() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported.");
    }

    public <T> T getBusinessObject(Class<T> arg0) throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported.");
    }

    public Class getInvokedBusinessInterface() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported.");
    }

    public EJBHome getEJBHome() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public EJBLocalHome getEJBLocalHome() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public Properties getEnvironment() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public Identity getCallerIdentity() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public Principal getCallerPrincipal() {
        if (principal == null) {
            throw new IllegalStateException("No fake principal set in test session context!");
        }
        return principal;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }

    public void addRole(String roleName) {
        roles.add(roleName);
    }

    public boolean isCallerInRole(Identity identity) {
        return roles.contains(identity.getName());
    }

    public boolean isCallerInRole(String roleName) {
        return roles.contains(roleName);
    }

    public UserTransaction getUserTransaction() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported.");
    }

    public void setRollbackOnly() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean getRollbackOnly() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported.");
    }

    public TimerService getTimerService() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported.");
    }

    public Object lookup(String arg0) {
        throw new UnsupportedOperationException("Not supported.");
    }
}
