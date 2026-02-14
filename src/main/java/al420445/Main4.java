package al420445;

import al420445.airport.Airport;
import al420445.airport.Passenger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main4 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernate2.ex1");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        var cedric = em.find(Passenger.class, 7);
//        final Passenger cedric = new Passenger("Cedric");
//        em.persist(cedric);

        em.getTransaction().commit();
        em.close();

        System.out.println(cedric);

        em = emf.createEntityManager();
        em.getTransaction().begin();

        em.merge(cedric);
        var cedric2 = em.find(Passenger.class, 7);
        var airport = em.find(Airport.class, 1);
        cedric2.setAirport(airport);

        em.getTransaction().commit();
        em.close();

        System.out.println(cedric2);

        em = emf.createEntityManager();
        em.getTransaction().begin();

        var cedric3 = em.find(Passenger.class, 7);
        em.remove(cedric3);

        em.getTransaction().commit();
        em.close();

    }
}
