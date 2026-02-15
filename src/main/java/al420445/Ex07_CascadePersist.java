package al420445;

import al420445.airport.OneWayTicket;
import al420445.airport.Passenger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.time.LocalDate;

/**
 * Ex07: Demonstrates cascade persist.
 * Adding a ticket to a managed passenger automatically persists the ticket
 * thanks to CascadeType.ALL on Passenger.tickets.
 */
public class Ex07_CascadePersist {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernate2.ex1");
        Ex01_PersistEntities.insertDataInDb(emf);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        final Passenger passenger = em.find(Passenger.class, 2L);
        final OneWayTicket oneWayTicket = new OneWayTicket(LocalDate.now());
        oneWayTicket.setNumber("AA12345");
        // Cascade: persisting the ticket is automatic via the managed passenger
        passenger.addTicket(oneWayTicket);

        em.getTransaction().commit();
        em.close();
        emf.close();
    }
}
