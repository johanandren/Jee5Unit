package com.markatta.jee5unit.core.db;

/**
 *
 * @author johan
 */
public class Jee5UnitDatabaseException extends RuntimeException {

    public Jee5UnitDatabaseException(String message) {
        super(message);
    }

    public Jee5UnitDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
