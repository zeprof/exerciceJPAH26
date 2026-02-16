package al420445.dao;

import al420445.airport.Airport;
import al420445.airport.Passenger;

import jakarta.persistence.*;
import java.util.Collections;
import java.util.List;

/**
 * Alternative implementation of AirportDao using the Functional Interface approach.
 * This version uses TransactionExecutor with lambda expressions.
 *
 * Compare this with AirportDaoImpl to see the difference between:
 * - Functional Interface approach (this class)
 * - Template Method pattern (AirportDaoImpl)
 */
public class AirportDaoFunctional implements AirportDao {

    // Read-only: no transaction needed for simple SELECT queries
    public List<Airport> getAirports() {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("select a from Airport a", Airport.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void addPassenger(String name, int airportId) {
        TransactionExecutor.executeInTransaction(em -> {
            Airport airport = em.find(Airport.class, (long) airportId);
            if (airport == null) return null;

            Passenger newPassenger = new Passenger(name);
            airport.addPassenger(newPassenger);
            em.persist(newPassenger);
            return null;
        });
    }

    @Override
    public List<Passenger> findPassengersByName(String name) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Passenger> query = em.createQuery(
                    "select p from Passenger p where lower(p.name) like :name", Passenger.class);
            query.setParameter("name", name.toLowerCase() + "%");
            List<Passenger> result = query.getResultList();
            return result == null ? Collections.emptyList() : result;
        } finally {
            em.close();
        }
    }
}

