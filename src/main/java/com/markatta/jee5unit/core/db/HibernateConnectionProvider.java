package com.markatta.jee5unit.core.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import org.hibernate.HibernateException;
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;

/**
 *
 * @author johan
 */
public class HibernateConnectionProvider implements ConnectionProvider {

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

    public boolean isUnwrappableAs(Class type) {
        return false;
    }

    public <T> T unwrap(Class<T> type) {
        throw new UnsupportedOperationException("Not supported.");
    }
}
