package com.markatta.jee5unit.core;

import org.hibernate.dialect.HSQLDialect;

/**
 * Bugfix for schema creation with Hsql according to HHH-7002.
 * Use this dialect instead of the default HSQLDialect
 * 
 * @author Johan Andren <johan.andren@mejsla.se>
 */
public class HsqlDialectFix extends HSQLDialect {

    @Override
    protected String getDropSequenceString(String sequenceName) {
        // Adding the "if exists" clause to avoid warnings
        return "drop sequence if exists " + sequenceName;
    }

    @Override
    public boolean dropConstraints() {
        // We don't need to drop constraints before dropping tables, that just leads to error
        // messages about missing tables when we don't have a schema in the database
        return false;
    }
    
    
    
}
