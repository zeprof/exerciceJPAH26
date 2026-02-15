package al420445;

import al420445.airport.Passenger;
import al420445.airport.Ticket;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.sql.SQLException;

/**
 * Ex08: Demonstrates navigating relationships.
 * Loads a passenger and iterates over their tickets,
 * showing lazy-loading within an open persistence context.
 */
public class Ex08_NavigateRelations {
    public static void main(String[] args) throws SQLException, InterruptedException {
        TcpServer.createTcpServer();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernate2.ex1");
        Ex01_PersistEntities.insertDataInDb(emf);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        final Passenger passenger = em.find(Passenger.class, 1L);
        for (Ticket ticket : passenger.getTickets()) {
            System.out.println(ticket.getNumber());
        }

        em.getTransaction().commit();
        em.close();
        emf.close();

        // Keep JVM alive so the H2 TCP server remains accessible
        Thread.currentThread().join();
    }
}
