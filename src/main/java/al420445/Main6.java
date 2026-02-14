package al420445;

import al420445.airport.Airport;
import al420445.airport.BebelleDTO;

import al420445.airport.Passenger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.sql.SQLException;
import java.util.List;

public class Main6 {
    public static void main(String[] args) throws InterruptedException, SQLException {
        TcpServer.createTcpServer();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernate2.ex1");
        Main.insertDataInDb(emf);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        final TypedQuery<Passenger> pass =
                em.createQuery(
            "select p from Passenger p where lower(p.airport.name) like 'henri%'", Passenger.class);
        final List<Passenger> passengers = pass.getResultList();
        System.out.println(passengers);

        final TypedQuery<Airport> airportQuery =
                em.createQuery(
            "select a from Airport a join fetch a.passengers where lower(a.name) like 'henri%'", Airport.class);
        final Airport airport = airportQuery.getSingleResult();
        System.out.println(airport);

        var queryStr = """
                select new al420445.airport.BebelleDTO(count(t), t.passenger) 
                from Ticket t 
                group by t.passenger
        """;
        final List<BebelleDTO> bebelleDTOS = em.createQuery(
                queryStr, BebelleDTO.class).getResultList();
        bebelleDTOS.forEach(o -> {
            System.out.println(o.count() + " " + o.passenger());
        });

        em.getTransaction().commit();
        em.close();
        emf.close();

        //System.out.println(airport.getPassengers().size());

        Thread.currentThread().join();
    }
}
