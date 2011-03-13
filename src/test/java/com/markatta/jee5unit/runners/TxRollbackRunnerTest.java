package com.markatta.jee5unit.runners;

import com.markatta.jee5unit.framework.EntityTransactional;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(TxRollbackRunner.class)
public class TxRollbackRunnerTest implements EntityTransactional {

    private boolean beginWasCalled;

    private boolean rollbackWasCalled;

    private boolean active;

    private boolean commitWasCalled;

    public EntityTransaction getTransaction() {
        return new EntityTransaction() {

            public void begin() {
                beginWasCalled = true;
                active = true;
            }

            public void commit() {
                commitWasCalled = true;
                active = false;
            }

            public void rollback() {
                rollbackWasCalled = true;
                active = false;
            }

            public void setRollbackOnly() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public boolean getRollbackOnly() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public boolean isActive() {
                return active;
            }
        };
    }

    @BeforeTestTransaction
    public void beforeTransaction() {
        assertFalse(active);
    }

    @AfterTestTransaction
    public void afterTransaction() {
        assertFalse(active);
        assertTrue(rollbackWasCalled);
        assertFalse(commitWasCalled);
    }

    @Before
    public void beforeTestInTransaction() {
        assertTrue(active);
    }

    @After
    public void afterTestInTransaction() {
        assertTrue(active);
        assertFalse(rollbackWasCalled);
    }

    @Test
    public void testMethod() {
        assertTrue(beginWasCalled);
        assertFalse(commitWasCalled);
        assertFalse(rollbackWasCalled);
        assertTrue(active);
    }

}
