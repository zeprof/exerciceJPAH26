package al420445;

import al420445.airport.*;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Ex01: Demonstrates persisting entities with relationships.
 * Shows @OneToMany, @ManyToOne, @Embedded, and @Inheritance.
 */
public class Ex01_PersistEntities {

    public static void main(String[] args) throws InterruptedException, SQLException {
        // Start H2 TCP server so you can inspect the DB with a SQL client
        // Configuration
        TcpServer.createTcpServer();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernate2.ex1");

        insertDataInDb(emf);

        // Utilisation
        var em = emf.createEntityManager();
        em.getTransaction().begin();

        final Airport airport = em.find(Airport.class, 1L);
        em.getTransaction().commit();

        System.out.println(airport.getPassengers().size());
        em.close();

        emf.close();

        // Keep JVM alive so the H2 TCP server remains accessible
        Thread.currentThread().join();
    }
    public static Result insertDataInDb() {
        var emf = Persistence.createEntityManagerFactory("hibernate2.ex1");
        return insertDataInDb(emf);
    }

    public static Result insertDataInDb(EntityManagerFactory emf) {
        var em = emf.createEntityManager();
        em.getTransaction().begin();

        Airport airport = new Airport("Henri Coanda");

        Passenger passenger1 = new Passenger("Moukaila Smith");
        airport.addPassenger(passenger1);
        Address address = new Address("street", "city", "province", "H1A 1A1");
        passenger1.setAddress(address);

        OneWayTicket ticket1 = new OneWayTicket();
        ticket1.setNumber("AA1234");
        ticket1.setLatestDepartureDate(LocalDate.now());
        passenger1.addTicket(ticket1);

        ReturnTicket ticket2 = new ReturnTicket();
        ticket2.setNumber("BB5678");
        ticket2.setLatestReturnDate(LocalDate.now());
        passenger1.addTicket(ticket2);

        Passenger passenger2 = new Passenger("Michael Johnson");
        airport.addPassenger(passenger2);

        OneWayTicket ticket3 = new OneWayTicket();
        ticket3.setNumber("CC6767");
        ticket3.setLatestDepartureDate(LocalDate.now());
        passenger2.addTicket(ticket3);

        em.persist(airport);
        em.persist(passenger1);
        em.persist(passenger2);

        em.getTransaction().commit();
        em.close();

        return new Result(airport, passenger1, passenger2);
    }

    public record Result(Airport airport, Passenger passenger1, Passenger passenger2) {
    }
}
