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

/**
 * Ex05: Demonstrates JPQL queries.
 * Shows how to query entities using JPQL with parameters.
 */
public class Ex05_JpqlQuery {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernate2.ex1");
        Ex01_PersistEntities.insertDataInDb(emf);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        final TypedQuery<Airport> query =
                em.createQuery("select a from Airport a where lower(a.name) like 'henri%'", Airport.class);
        final Airport airport = query.getSingleResult();

        var pass = new Passenger("Christopher");
        airport.addPassenger(pass);
        pass.addTicket(new OneWayTicket(LocalDate.now()));
        pass.addTicket(new ReturnTicket(LocalDate.now().plusDays(10)));
        em.persist(pass);

        em.getTransaction().commit();
        em.close();
        emf.close();
    }
}
