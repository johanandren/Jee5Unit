package com.markatta.jee5unit.core.db;

import com.markatta.jee5unit.core.Jee5UnitConfiguration;
import com.markatta.jee5unit.core.Jee5UnitConfigurationException;
import com.markatta.jee5unit.core.Jee5UnitRuntimeException;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton that houses the current ConnectionProvider. Needed to make it possible
 * to create the {@link HibernateConnectionProvider} that needs a no-args constructor.
 *
 * @author johan
 */
public class CurrentConnection {

    private static CurrentConnection instance;

    private ConnectionProvider provider;

    private CurrentConnection(ConnectionProvider provider) {
        this.provider = provider;
    }

    /**
     * Setup a database connection by reading the configuration file.
     */
    public static void setup() {
        Properties properties = Jee5UnitConfiguration.getConfiguration();
        String databaseProvider = properties.getProperty(Jee5UnitConfiguration.KEY_DATABASE_PROVIDER);

        try {
            Class providerClass = Class.forName("com.markatta.jee5unit.core.db." + databaseProvider);
            
            ConnectionProvider provider = (ConnectionProvider) providerClass.newInstance();
            instance = new CurrentConnection(provider);

        } catch (ClassNotFoundException ex) {
            throw new Jee5UnitConfigurationException("Database connection provider konfiguration key " + Jee5UnitConfiguration.KEY_DATABASE_PROVIDER +
                    " set to " + databaseProvider + " but no such provider exists.");
        } catch (IllegalAccessException ex) {
            throw new Jee5UnitRuntimeException("Failed to instanciate the database connection provider " + databaseProvider, ex);
        } catch (InstantiationException ex) {
            throw new Jee5UnitRuntimeException("Failed to instanciate the database connection provider " + databaseProvider, ex);
        }

    }

    public static void setup(ConnectionProvider provider) {
        instance = new CurrentConnection(provider);
        try {
            // make sure the database is working
            provider.getConnection().close();
        } catch (SQLException ex) {
            throw new Jee5UnitDatabaseException("Could not create connection to database", ex);
        }
    }

    public static ConnectionProvider getProvider() {
        return instance.provider;
    }
}
