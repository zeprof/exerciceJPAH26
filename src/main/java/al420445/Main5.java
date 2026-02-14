package al420445;

import al420445.airport.Airport;
import al420445.airport.OneWayTicket;
import al420445.airport.Passenger;
import al420445.airport.ReturnTicket;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;

public class Main5 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernate2.ex1");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        final TypedQuery<Airport> query =
                em.createQuery("select a from Airport a where lower(a.name) like 'henri%'", Airport.class);
        final Airport airport = query.getSingleResult();
        var pass = new Passenger("Christopher");
        pass.setAirport(airport);
        pass.addTicket(new OneWayTicket(LocalDate.now()));
        pass.addTicket(new ReturnTicket(LocalDate.now().plusDays(10)));
        em.persist(pass);

        em.getTransaction().commit();
        em.close();
    }
}
