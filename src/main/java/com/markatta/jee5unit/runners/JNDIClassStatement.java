package com.markatta.jee5unit.runners;

import org.junit.runners.model.Statement;

/**
 *
 * @author johan
 */
class JNDIClassStatement extends Statement {

    private final Statement child;

    public JNDIClassStatement(Statement child) {
        this.child = child;
    }

    @Override
    public void evaluate() throws Throwable {
        // make sure java will use our in memory context factory
        System.setProperty("java.naming.factory.initial", "com.markatta.jee5unit.jndi.InMemoryContextFactory");

        child.evaluate();

        System.clearProperty("java.naming.factory.initial");
    }
}
