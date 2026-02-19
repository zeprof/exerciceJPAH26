package al420445;

import al420445.airport.Airport;
import al420445.airport.PassengerTicketCountDTO;

import al420445.airport.Passenger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.sql.SQLException;
import java.util.List;

/**
 * Ex06: Demonstrates JOIN FETCH and DTO projections in JPQL.
 * - JOIN FETCH avoids the N+1 query problem
 * - DTO projection with 'new' in JPQL returns lightweight objects
 */
public class Ex06_JoinFetchAndDTO {
    public static void main(String[] args) throws InterruptedException, SQLException {
        TcpServer.createTcpServer();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernate2.ex1");
        Ex01_PersistEntities.insertDataInDb(emf);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        // Query navigating a relationship (implicit join)
        final TypedQuery<Passenger> pass =
                em.createQuery(
            "select p from Passenger p where lower(p.airport.name) like 'henri%'", Passenger.class);
        final List<Passenger> passengers = pass.getResultList();
        System.out.println(passengers);

        // JOIN FETCH: loads passengers in a single query (avoids N+1)
        final TypedQuery<Airport> airportQuery =
                em.createQuery(
            "select a from Airport a join fetch a.passengers where lower(a.name) like 'henri%'", Airport.class);
        final Airport airport = airportQuery.getSingleResult();
        System.out.println(airport);

        // DTO projection: aggregate query returning a custom record
        var queryStr = """
                select new al420445.airport.PassengerTicketCountDTO(count(t), t.passengerXXXX) 
                from Ticket t 
                group by t.passengerXXXX
        """;
        final List<PassengerTicketCountDTO> ticketCounts = em.createQuery(
                queryStr, PassengerTicketCountDTO.class).getResultList();
        ticketCounts.forEach(dto ->
            System.out.println(dto.count() + " ticket(s) for " + dto.passenger())
        );

        em.getTransaction().commit();
        em.close();
        emf.close();

        // Keep JVM alive so the H2 TCP server remains accessible
        Thread.currentThread().join();
    }
}
