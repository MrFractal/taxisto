package ru.trendtech.services.versioning;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.sql.Connection;
import java.sql.SQLException;

class IsolationSupportHibernateJpaDialect extends HibernateJpaDialect {

    private final Logger logger = LoggerFactory.getLogger(IsolationSupportHibernateJpaDialect.class);

    /**
     * This method is overridden to set custom isolation levels on the
     * connection
     *
     * @param entityManager entityManager
     * @param definition    transaction definition
     * @return Object transaction
     * @throws PersistenceException
     * @throws SQLException
     * @throws TransactionException
     */
    @Override
    public Object beginTransaction(final EntityManager entityManager,
                                   final TransactionDefinition definition) throws PersistenceException,
            SQLException, TransactionException {
        Session session = (Session) entityManager.getDelegate();
        if (definition.getTimeout() != TransactionDefinition.TIMEOUT_DEFAULT) {
            getSession(entityManager).getTransaction().setTimeout(definition.getTimeout());
        }

        entityManager.getTransaction().begin();
        logger.debug("Transactions started");

        session.doWork(new Work() {

            public void execute(Connection connection) throws SQLException {
                logger.debug("The connection instance is {}", connection);
                logger.debug("The isolation level of the connection is {} and the isolation level set on the transaction is {}",
                        connection.getTransactionIsolation(), definition.getIsolationLevel());
                DataSourceUtils.prepareConnectionForTransaction(connection, definition);
            }
        });

        return prepareTransaction(entityManager, definition.isReadOnly(), definition.getName());
    }


/*
  @Override
  public void cleanupTransaction(Object transactionData) {
    super.cleanupTransaction(((IsolationSupportSessionTransactionData) transactionData).getSessionTransactionDataFromHibernateTemplate());
    ((IsolationSupportSessionTransactionData) transactionData).resetIsolationLevel();
  }
*/

    private static class IsolationSupportSessionTransactionData {

        private final Object sessionTransactionDataFromHibernateJpaTemplate;

        private final Integer previousIsolationLevel;

        private final Connection connection;

        public IsolationSupportSessionTransactionData(Object sessionTransactionDataFromHibernateJpaTemplate, Integer previousIsolationLevel, Connection connection) {
            this.sessionTransactionDataFromHibernateJpaTemplate = sessionTransactionDataFromHibernateJpaTemplate;
            this.previousIsolationLevel = previousIsolationLevel;
            this.connection = connection;
        }

        public void resetIsolationLevel() {
            if (this.previousIsolationLevel != null) {
                DataSourceUtils.resetConnectionAfterTransaction(connection, previousIsolationLevel);
            }
        }

        public Object getSessionTransactionDataFromHibernateTemplate() {
            return this.sessionTransactionDataFromHibernateJpaTemplate;
        }

    }

}