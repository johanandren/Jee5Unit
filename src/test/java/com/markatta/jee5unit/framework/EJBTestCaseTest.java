package com.markatta.jee5unit.framework;


import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author johan
 */
public class EJBTestCaseTest extends EJBTestCase<FakeEjb> {

    public EJBTestCaseTest() {
        super(FakeEjb.class);
    }

    @Test
    public void testEntityManagerIsInjected() {
        FakeEjb ejb = getBeanToTest();
        assertNotNull("No ejb instance returned from getBeanToTest()", ejb);
        assertNotNull("No entity manager injected into EJB", ejb.getEm());
    }

    @Test
    public void testEJBInjection() {
        FakeEjb ejb = getBeanToTest();

        assertNull(ejb.getOtherFakeEJB());
        
        OtherFakeEJBBean mockEJB = new OtherFakeEJBBean();

        injectEJB(OtherFakeEJBLocal.class, mockEJB);

        assertNotNull(ejb.getOtherFakeEJB());
    }

}
