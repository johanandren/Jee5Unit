package com.markatta.jee5unit.framework;

import javax.persistence.EntityTransaction;

/**
 * Simple transaction interface.
 * @author johan
 */
public interface EntityTransactional {

    public EntityTransaction getTransaction();
}
