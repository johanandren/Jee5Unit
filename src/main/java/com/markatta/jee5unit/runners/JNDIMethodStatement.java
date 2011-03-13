package com.markatta.jee5unit.runners;

import com.markatta.jee5unit.jndi.InMemoryContextFactory;
import org.junit.runners.model.Statement;

/**
 *
 * @author johan
 */
class JNDIMethodStatement extends Statement {

    private final Statement child;

    public JNDIMethodStatement(Statement child) {
        this.child = child;
    }

    @Override
    public void evaluate() throws Throwable {

        try {
            child.evaluate();
            InMemoryContextFactory.clear();
            
        } catch (Throwable ex) {
            
            // make sure jndi context is cleared after each method
            // even if they caused an error
            InMemoryContextFactory.clear();
            throw ex;
        }
    }
}
