package com.markatta.jee5unit.core.db;

import java.sql.Connection;

/**
 *
 * @author johan
 */
public interface ConnectionProvider {

    public Connection getConnection();

    public String getHibernateDialect();
}
