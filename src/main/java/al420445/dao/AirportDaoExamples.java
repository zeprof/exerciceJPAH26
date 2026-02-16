package al420445.dao;

import al420445.airport.Airport;
import al420445.airport.Passenger;
import jakarta.persistence.EntityManager;

/**
 * Example showing additional uses of the TransactionalDao Template Method pattern.
 * This demonstrates how to handle different return types and operations.
 */
public class AirportDaoExamples {

    /**
     * Example 1: Creating and returning an entity
     */
    public Airport createAirport(String name) {
        return new TransactionalDao<Airport>() {
            @Override
            protected Airport executeInTransaction(EntityManager em) {
                Airport airport = new Airport(name);
                em.persist(airport);
                return airport;
            }
        }.executeInTransaction();
    }

    /**
     * Example 2: Updating an entity
     */
    public void updateAirportName(Long airportId, String newName) {
        new TransactionalDao<Void>() {
            @Override
            protected Void executeInTransaction(EntityManager em) {
                Airport airport = em.find(Airport.class, airportId);
                if (airport != null) {
                    airport.setName(newName);
                    // No need to call em.merge() - entity is managed
                }
                return null;
            }
        }.executeInTransaction();
    }

    /**
     * Example 3: Deleting an entity
     */
    public void deleteAirport(Long airportId) {
        new TransactionalDao<Void>() {
            @Override
            protected Void executeInTransaction(EntityManager em) {
                Airport airport = em.find(Airport.class, airportId);
                if (airport != null) {
                    em.remove(airport);
                }
                return null;
            }
        }.executeInTransaction();
    }

    /**
     * Example 4: Returning a primitive/wrapper type
     */
    public Long countPassengers(Long airportId) {
        return new TransactionalDao<Long>() {
            @Override
            protected Long executeInTransaction(EntityManager em) {
                return em.createQuery(
                    "select count(p) from Passenger p where p.airport.id = :airportId",
                    Long.class)
                    .setParameter("airportId", airportId)
                    .getSingleResult();
            }
        }.executeInTransaction();
    }

    /**
     * Example 5: Transferring a passenger between airports
     */
    public void transferPassenger(Long passengerId, Long newAirportId) {
        new TransactionalDao<Void>() {
            @Override
            protected Void executeInTransaction(EntityManager em) {
                Passenger passenger = em.find(Passenger.class, passengerId);
                Airport newAirport = em.find(Airport.class, newAirportId);

                if (passenger != null && newAirport != null) {
                    // Simply update the relationship - JPA handles the rest
                    newAirport.addPassenger(passenger);
                }
                return null;
            }
        }.executeInTransaction();
    }

    /**
     * Example 6: Returning a boolean result
     */
    public boolean airportExists(String name) {
        return new TransactionalDao<Boolean>() {
            @Override
            protected Boolean executeInTransaction(EntityManager em) {
                Long count = em.createQuery(
                    "select count(a) from Airport a where a.name = :name",
                    Long.class)
                    .setParameter("name", name)
                    .getSingleResult();
                return count > 0;
            }
        }.executeInTransaction();
    }
}


