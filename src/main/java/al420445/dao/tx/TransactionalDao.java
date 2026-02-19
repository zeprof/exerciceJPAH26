package al420445.dao.tx;

import al420445.dao.base.JpaUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

/**
 * Abstract base class implementing the Template Method pattern
 * for managing JPA transactions.
 *
 * Subclasses implement the executeInTransaction(EntityManager) method
 * to define the business logic that should run within a transaction.
 */
public abstract class TransactionalDao<T> {

    /**
     * Template method that manages the transaction lifecycle.
     * Handles begin/commit/rollback/close automatically.
     *
     * Subclasses call this method and implement executeInTransaction()
     * to define what should happen within the transaction.
     */
    public final T executeInTransaction() {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T result = executeInTransaction(em);
            tx.commit();
            return result;
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    /**
     * Abstract method to be implemented by subclasses.
     * Contains the business logic to execute within a transaction.
     *
     * @param em the EntityManager to use for database operations
     * @return the result of the operation (can be null)
     */
    protected abstract T executeInTransaction(EntityManager em);
}

