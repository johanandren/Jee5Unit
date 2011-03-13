package com.markatta.jee5unit.core.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author johan
 */
public class InMemoryDerby implements ConnectionProvider {

    private static final String URL = "jdbc:derby:memory:jee5unittestdb;create=true";

    private Connection connection;

    public Connection getConnection() {

        if (connection == null) {

            try {
                return DriverManager.getConnection(URL);
            } catch (SQLException ex) {
                throw new Jee5UnitDatabaseException("Failed to create/connect derby in memory database. " +
                        "Make sure that you have the derby jar file on classpath.", ex);
            }
        }
        return connection;

    }

    public String getHibernateDialect() {
        return "org.hibernate.dialect.DerbyDialect";
    }
}
