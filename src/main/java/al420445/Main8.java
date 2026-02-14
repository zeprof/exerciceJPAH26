package al420445;

import al420445.airport.Passenger;
import al420445.airport.Ticket;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.sql.SQLException;

public class Main8 {
    public static void main(String[] args) throws InterruptedException, SQLException {
        TcpServer.createTcpServer();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernate2.ex1");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        final Main.Result result = Main.insertDataInDb(emf);
        em.persist(result.airport());
        em.persist(result.passenger1());
        em.persist(result.passenger2());
        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        em.getTransaction().begin();
        final Passenger passenger = em.find(Passenger.class, 2);
        for (Ticket ticket: passenger.getTickets()) {
            System.out.println(ticket.getNumber());
        }

        em.getTransaction().commit();
        em.close();

        Thread.currentThread().join();
    }
}
