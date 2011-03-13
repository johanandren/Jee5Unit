package com.markatta.jee5unit.core;

import com.markatta.jee5unit.core.HibernateConfiguration;
import javax.persistence.EntityManager;
import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author johan
 */
public class HibernateConfigurationTest extends TestCase {

    @Test
    public void testCreateEntityManager() throws Exception {
        HibernateConfiguration configuration = new HibernateConfiguration();
        EntityManager em = configuration.createEntityManager();
        
        // FakeEntity should be mapped
        em.createQuery("SELECT f FROM FakeEntity f").getResultList();
    }

}