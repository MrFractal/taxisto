package ru.trendtech.services.resources;

/**
 * Created by petr on 08.07.2015.
 */

import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;

public class CustomHibernateJpaDialect extends HibernateJpaDialect {

    ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();
    ThreadLocal<Integer> originalIsolation = new ThreadLocal<>();

    @Override
    public Object beginTransaction(EntityManager entityManager,
                                   TransactionDefinition definition)
            throws PersistenceException, SQLException, TransactionException {

        boolean readOnly = definition.isReadOnly();
        Connection connection =
                this.getJdbcConnection(entityManager, readOnly).getConnection();
        connectionThreadLocal.set(connection);
        originalIsolation.set(DataSourceUtils
                .prepareConnectionForTransaction(connection, definition));

        entityManager.getTransaction().begin();

        return prepareTransaction(entityManager, readOnly, definition.getName());
    }

    /*

     We just have to trust that spring won't forget to call us. If they forget,
     we get a thread-local/classloader memory leak and messed up isolation
     levels. The finally blocks on line 805 and 876 of
     AbstractPlatformTransactionManager (Spring 3.2.3) seem to ensure this,
     though there is a bit of logic between there and the actual call to this
     method.

     */
    @Override
    public void cleanupTransaction(Object transactionData) {
        try {
            super.cleanupTransaction(transactionData);
            DataSourceUtils.resetConnectionAfterTransaction(
                    connectionThreadLocal.get(), originalIsolation.get());
        } finally {
            connectionThreadLocal.remove();
            originalIsolation.remove();
        }
    }

}