package com.markatta.jee5unit.framework;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author johan
 */
public class FakeEjb {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private OtherFakeEJBLocal otherFakeEJB;

    public EntityManager getEm() {
        return em;
    }

    public OtherFakeEJBLocal getOtherFakeEJB() {
        return otherFakeEJB;
    }
    
}
