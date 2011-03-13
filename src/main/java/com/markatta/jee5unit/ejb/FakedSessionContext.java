package com.markatta.jee5unit.ejb;

import java.security.Identity;
import java.security.Principal;
import java.util.Properties;
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

    public EJBLocalObject getEJBLocalObject() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public EJBObject getEJBObject() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public MessageContext getMessageContext() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public <T> T getBusinessObject(Class<T> arg0) throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Class getInvokedBusinessInterface() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public EJBHome getEJBHome() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public EJBLocalHome getEJBLocalHome() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Properties getEnvironment() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Identity getCallerIdentity() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Principal getCallerPrincipal() {
        return principal;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }

    public boolean isCallerInRole(Identity arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isCallerInRole(String arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public UserTransaction getUserTransaction() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setRollbackOnly() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean getRollbackOnly() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public TimerService getTimerService() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object lookup(String arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
