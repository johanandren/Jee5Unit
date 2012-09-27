package com.markatta.jee5unit.framework;

import javax.naming.InitialContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class JNDITest extends EntityTestCase {

    @Test
    public void testThatStuffCanBePublished() throws Exception {
        publishOnJNDI("somekey", this);
        
        InitialContext context = new InitialContext();
        assertNotNull(context.lookup("somekey"));
    }
    
    @Test
    public void testThatSessionContextIsPublished() throws Exception {
        InitialContext context = new InitialContext();
        assertNotNull(context.lookup("java:comp/EJBContext"));
        assertNotNull(context.lookup("java:comp/env/sessionContext"));
    }
}
