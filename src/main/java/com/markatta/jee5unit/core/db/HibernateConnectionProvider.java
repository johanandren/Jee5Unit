package com.markatta.jee5unit.core.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import org.hibernate.HibernateException;

/**
 *
 * @author johan
 */
public class HibernateConnectionProvider implements org.hibernate.connection.ConnectionProvider {

    private Connection connection;

    public HibernateConnectionProvider() {
    }

    public void configure(Properties configuration) throws HibernateException {
        // ignore
    }

    public Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = CurrentConnection.getProvider().getConnection();

        }
        return connection;
    }

    public void closeConnection(Connection connection) throws SQLException {
        // never close our connection
    }

    public void close() throws HibernateException {
        // we never close our connection
    }

    public boolean supportsAggressiveRelease() {
        return false;
    }
}
