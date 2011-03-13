package com.markatta.jee5unit.core;

import com.markatta.jee5unit.core.db.CurrentConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.hibernate.event.PreInsertEventListener;
import org.hibernate.event.PreUpdateEventListener;
import org.hibernate.validator.event.ValidateEventListener;
import org.scannotation.ClasspathUrlFinder;
import org.scannotation.AnnotationDB;

/**
 * Singleton that houses the hibernate entity manager factory
 * 
 * @author johan
 */
public class HibernateConfiguration {

    private static final String USER_CONFIG_PATH = "/jee5unit.hibernate.properties";

    private static final Logger logger = Logger.getLogger("HibernateConfiguration");

    private static final HibernateConfiguration instance = new HibernateConfiguration();

    private EntityManagerFactory emf;

    /**
     * Non singleton usage possible for testing purposes
     */
    HibernateConfiguration() {

        Properties jee5Configuration = Jee5UnitConfiguration.getConfiguration();

        CurrentConnection.setup();

        Ejb3Configuration configuration = new Ejb3Configuration();

        configuration.setProperty(Environment.CONNECTION_PROVIDER, "com.markatta.jee5unit.core.db.HibernateConnectionProvider");
        configuration.setProperty("hibernate.dialect", CurrentConnection.getProvider().getHibernateDialect());
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        configuration.setProperty("hibernate.jdbc.batch_size", "0");

        // Disable second level cache to avoid problems with stale data across
        // tests and other errors (READ_ONLY for example) or else hibernate
        // will bail when no second level cacge implementation is available
        // and there are @Cache annotated classes among the entities
        configuration.setProperty("hibernate.cache.use_second_level_cache", "false");
        configuration.setProperty("hibernate.cache.use_query_cache", "false");

        Properties userconfig = loadUserConfiguration();
        if (userconfig != null) {
            logger.info("Using custom hibernate configuration.");
            for (Object key : userconfig.keySet()) {
                configuration.setProperty((String)key, userconfig.getProperty((String)key));
            }
        }

        try {

            // find all annotated entities in classpath entries that has
            // a META-INF/persistence.xml
            URL[] urls = ClasspathUrlFinder.findResourceBases("META-INF/persistence.xml");
            AnnotationDB db = new AnnotationDB();
            db.scanArchives(urls);
            Set<String> entityClasses = db.getAnnotationIndex().get(javax.persistence.Entity.class.getName());

            ClassLoader cl = getClass().getClassLoader();
            for (String className : entityClasses) {
                Class entityClass = cl.loadClass(className);

                configuration.addAnnotatedClass(entityClass);
            }

            if ("true".equals(jee5Configuration.getProperty(Jee5UnitConfiguration.KEY_DISABLE_HIBERNATE_VALIDATORS))) {
                removeHibernateValidationListenersIfPresent(configuration);
            }


            emf = configuration.createEntityManagerFactory();

        } catch (IOException ex) {
            throw new RuntimeException("Error during search for annotated classes", ex);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Failed to load entity class", ex);
        }
    }

    public static HibernateConfiguration getInstance() {
        return instance;
    }

    public EntityManager createEntityManager() {
        return emf.createEntityManager();
    }

    public SessionFactory getSessionFactory() {
        return ((HibernateEntityManagerFactory) emf).getSessionFactory();
    }

    private void removeHibernateValidationListenersIfPresent(Ejb3Configuration configuration) {
        try {

            configuration.setProperty("hibernate.validator.autoregister_listeners", "false");

            // this is done to avoid having a dependency on hibernate validation
            // if the using proect has no dependencies on hibernate validation
            // this will throw an exception
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            cl.loadClass("org.hibernate.validator.event.ValidateEventListener");

            logger.info("Found hibernate validator on classpath, disabling event listeners");

            // due to some kind of bug hibernate does not always care about the property
            // hibernate.validator.autoregister_listeners that the hibernate
            // documentation says should disable them. We remove them manually here.
            // TODO: need a reference to a hibernate jira issue
            {
                PreInsertEventListener[] listeners = configuration.getEventListeners().getPreInsertEventListeners();
                ArrayList<PreInsertEventListener> newListeners = new ArrayList<PreInsertEventListener>();
                for (PreInsertEventListener listener : listeners) {
                    if (!(listener instanceof ValidateEventListener)) {
                        newListeners.add(listener);
                    }
                }
                configuration.setListeners("pre-insert", newListeners.toArray(new PreInsertEventListener[0]));
            }
            {
                PreUpdateEventListener[] listeners = configuration.getEventListeners().getPreUpdateEventListeners();
                ArrayList<PreUpdateEventListener> newListeners = new ArrayList<PreUpdateEventListener>();
                for (PreUpdateEventListener listener : listeners) {
                    if (!(listener instanceof ValidateEventListener)) {
                        newListeners.add(listener);
                    }
                }
                configuration.setListeners("pre-update", newListeners.toArray(new PreUpdateEventListener[0]));
            }
        } catch (ClassNotFoundException ex) {
            // hibernate validation not available, do not remove listeners
        }
    }

    private static Properties loadUserConfiguration() {
        Properties configOnClasspath = new Properties();

        InputStream input = Jee5UnitConfiguration.class.getResourceAsStream(USER_CONFIG_PATH);
        if (input == null) {
            logger.info("Using default hibernate configuration.");
        } else {
            try {
                configOnClasspath.load(input);

            } catch (IOException ex) {
                logger.log(Level.WARNING, "Failed to load configuration file 'jee5unit.hibernate.properties' from classpath, will use defaults", ex);
            }
        }

        return configOnClasspath;
    }
}
