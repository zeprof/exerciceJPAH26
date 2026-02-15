package al420445;

import al420445.airport.Airport;
import al420445.airport.Passenger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Ex04: Demonstrates entity lifecycle: detach, merge, and remove.
 * Shows how entities become detached when the EntityManager closes,
 * and how merge() re-attaches them.
 */
public class Ex04_MergeAndRemove {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernate2.ex1");
        Ex01_PersistEntities.insertDataInDb(emf);

        // --- Step 1: Find a passenger (becomes detached after em.close) ---
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        var passenger = em.find(Passenger.class, 1L);
        em.getTransaction().commit();
        em.close();

        System.out.println("Detached: " + passenger);

        // --- Step 2: Merge detached entity into new persistence context ---
        em = emf.createEntityManager();
        em.getTransaction().begin();

        var managed = em.merge(passenger);
        var airport = em.find(Airport.class, 1L);
        managed.setAirport(airport);

        em.getTransaction().commit();
        em.close();

        System.out.println("After merge: " + managed);

        // --- Step 3: Remove entity ---
        em = emf.createEntityManager();
        em.getTransaction().begin();

        var toRemove = em.find(Passenger.class, 1L);
        em.remove(toRemove);

        em.getTransaction().commit();
        em.close();
        emf.close();
    }
}
