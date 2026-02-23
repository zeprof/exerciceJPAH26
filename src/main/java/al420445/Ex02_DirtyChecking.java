package al420445;

import al420445.airport.Passenger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.sql.SQLException;

/**
 * Ex02: Demonstrates JPA dirty checking.
 * Modifying a managed entity inside a transaction automatically generates an UPDATE.
 */
public class Ex02_DirtyChecking {
    public static void main(String[] args) throws InterruptedException, SQLException {
        TcpServer.createTcpServer();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernate2.ex1");
        Ex01_PersistEntities.insertDataInDb(emf);

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        final Passenger p2 = em.find(Passenger.class, 1L);
        System.out.println(p2);
        // Dirty checking: this change is automatically persisted at commit
        //p2.setName("Manolo");

        em.getTransaction().commit();

        em.close();
        //emf.close();

        p2.setName("Manolo");

        em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(p2);
        em.getTransaction().commit();
        em.close();

        // Keep JVM alive so the H2 TCP server remains accessible

        Thread.currentThread().join();
    }
}
