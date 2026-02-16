package al420445.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.function.Function;

/**
 * Utility class for executing database operations within a transaction.
 * This class demonstrates the Functional Interface approach using Function<T,R>.
 *
 * This is an alternative to the Template Method pattern (TransactionalDao).
 */
public class TransactionExecutor {

    /**
     * Executes the given action within a transaction using a functional interface.
     * Handles begin/commit/rollback/close automatically.
     *
     * This approach uses Java 8+ lambda expressions and the Function interface.
     *
     * @param action A Function that takes an EntityManager and returns a result
     * @param <T> The type of result to return
     * @return The result of the action
     *
     * Example usage:
     * <pre>
     * TransactionExecutor.executeInTransaction(em -> {
     *     Airport airport = em.find(Airport.class, id);
     *     return airport;
     * });
     * </pre>
     */
    public static <T> T executeInTransaction(Function<EntityManager, T> action) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T result = action.apply(em);
            tx.commit();
            return result;
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }
}

