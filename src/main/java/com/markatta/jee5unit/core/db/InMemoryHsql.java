package com.markatta.jee5unit.core.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author johan
 */
public class InMemoryHsql implements ConnectionProvider {

    private static final String DRIVER_CLASS = "org.hsqldb.jdbcDriver";

    private static final String URL = "jdbc:hsqldb:mem:jee5unit";

    public Connection getConnection() {
        try {
            // Load derby jdbc driver
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException cnfe) {
            throw new Jee5UnitDatabaseException("Failed to load the hsql JDBC driver '" + DRIVER_CLASS + "'. " +
                    "Are you sure the hsql jars are on the classpath?");
        }

        try {

            return DriverManager.getConnection(URL);
        } catch (SQLException ex) {
            throw new Jee5UnitDatabaseException("Failed to create/connect derby in memory database", ex);
        }


    }

    public String getHibernateDialect() {
        return "com.markatta.jee5unit.core.HsqlDialectFix";
    }
}
