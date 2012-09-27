package com.markatta.jee5unit.runners;

import com.markatta.jee5unit.framework.HasGotJeeContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author johan
 */
@RunWith(Jee5UnitRunner.class)
public class Jee5UnitWithJNDIRunnerTest extends HasGotJeeContext {

    @Test
    public void firstTest() throws Exception {
        InitialContext context = new InitialContext();
        context.bind("some/path", this);
    }

    @Test(expected=NamingException.class)
    public void secondTest() throws Exception {
        InitialContext context = new InitialContext();
        // should not be available across the tests
        context.lookup("some/path");
    }

}
