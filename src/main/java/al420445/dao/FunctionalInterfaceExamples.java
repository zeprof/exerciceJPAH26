package al420445.dao;

import al420445.airport.Airport;
import al420445.airport.Passenger;
import jakarta.persistence.EntityManager;

/**
 * Examples showing the Functional Interface approach for transaction management.
 * Compare this with AirportDaoExamples.java to see the difference.
 *
 * This demonstrates using TransactionExecutor with lambda expressions.
 */
public class FunctionalInterfaceExamples {

    /**
     * Example 1: Creating and returning an entity
     */
    public Airport createAirport(String name) {
        return TransactionExecutor.executeInTransaction(em -> {
            Airport airport = new Airport(name);
            em.persist(airport);
            return airport;
        });
    }

    /**
     * Example 2: Updating an entity
     */
    public void updateAirportName(Long airportId, String newName) {
        TransactionExecutor.executeInTransaction(em -> {
            Airport airport = em.find(Airport.class, airportId);
            if (airport != null) {
                airport.setName(newName);
            }
            return null;
        });
    }

    /**
     * Example 3: Deleting an entity
     */
    public void deleteAirport(Long airportId) {
        TransactionExecutor.executeInTransaction(em -> {
            Airport airport = em.find(Airport.class, airportId);
            if (airport != null) {
                em.remove(airport);
            }
            return null;
        });
    }

    /**
     * Example 4: Returning a primitive/wrapper type
     */
    public Long countPassengers(Long airportId) {
        return TransactionExecutor.executeInTransaction(em -> {
            return em.createQuery(
                "select count(p) from Passenger p where p.airport.id = :airportId",
                Long.class)
                .setParameter("airportId", airportId)
                .getSingleResult();
        });
    }

    /**
     * Example 5: Transferring a passenger between airports
     */
    public void transferPassenger(Long passengerId, Long newAirportId) {
        TransactionExecutor.executeInTransaction(em -> {
            Passenger passenger = em.find(Passenger.class, passengerId);
            Airport newAirport = em.find(Airport.class, newAirportId);

            if (passenger != null && newAirport != null) {
                newAirport.addPassenger(passenger);
            }
            return null;
        });
    }

    /**
     * Example 6: Returning a boolean result
     */
    public boolean airportExists(String name) {
        return TransactionExecutor.executeInTransaction(em -> {
            Long count = em.createQuery(
                "select count(a) from Airport a where a.name = :name",
                Long.class)
                .setParameter("name", name)
                .getSingleResult();
            return count > 0;
        });
    }

    /**
     * Example 7: Multi-line lambda with more complex logic
     */
    public Airport createAirportWithPassengers(String airportName, String... passengerNames) {
        return TransactionExecutor.executeInTransaction(em -> {
            // Create airport
            Airport airport = new Airport(airportName);
            em.persist(airport);

            // Add passengers
            for (String passengerName : passengerNames) {
                Passenger passenger = new Passenger(passengerName);
                airport.addPassenger(passenger);
                em.persist(passenger);
            }

            return airport;
        });
    }
}

