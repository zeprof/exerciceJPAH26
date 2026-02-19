package al420445.dao.base;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Simple utility to manage a single EntityManagerFactory for the application.
 * Keeps lifecycle centralized so DAOs don't recreate the factory every call.
 */
public final class JpaUtil {

    private static final EntityManagerFactory EMF = buildEntityManagerFactory();

    private JpaUtil() {
    }

    private static EntityManagerFactory buildEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("hibernate2.ex1");
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return EMF;
    }

    public static void close() {
        if (EMF != null && EMF.isOpen()) {
            EMF.close();
        }
    }
}
