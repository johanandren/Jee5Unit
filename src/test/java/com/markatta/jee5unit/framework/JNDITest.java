package com.markatta.jee5unit.framework;

import com.markatta.jee5unit.runners.UsesJNDI;
import javax.naming.InitialContext;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author johan
 */
@UsesJNDI(publishEntityManager = true, entityManagerName = "test/entityManager")
public class JNDITest extends EntityTestCase {

    @Test
    public void testThatEntityManagerIsPublished() throws Exception {
        InitialContext context = new InitialContext();
        assertNotNull(context.lookup("test/entityManager"));
    }
}
