package com.markatta.jee5unit.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Loads the configuration file for jee5unit if it exists.
 *
 * @author johan
 */
public class Jee5UnitConfiguration {

    private static final Logger logger = Logger.getLogger("Jee5UnitConfiguration");

    public static final String KEY_DISABLE_HIBERNATE_VALIDATORS = "disable_hibernate_validators";

    public static final String KEY_DATABASE_PROVIDER = "database_provider";

    private static Properties configuration;

    private static Properties getDefaults() {
        Properties defaults = new Properties();
        defaults.setProperty(KEY_DISABLE_HIBERNATE_VALIDATORS, "true");
        defaults.setProperty(KEY_DATABASE_PROVIDER, "InMemoryHsql");

        return defaults;
    }

    private static Properties loadConfiguration() {
        Properties configOnClasspath = new Properties(getDefaults());

        InputStream input = Jee5UnitConfiguration.class.getResourceAsStream("/jee5unit.properties");
        if (input == null) {
            logger.info("No configuration file found, using default configuration. " +
                    "To provide custom configuration place a properties file calld jee5unit.properties in the " +
                    "default package on the classpath.");
        } else {
            try {
                configOnClasspath.load(input);

            } catch (IOException ex) {
                logger.log(Level.WARNING, "Failed to load configuration file 'jee5unit.properties' from classpath, will use defaults", ex);
            }
        }

        return configOnClasspath;
    }

    public static Properties getConfiguration() {
        if (configuration == null) {
            configuration = loadConfiguration();
        }

        return configuration;
    }
}
