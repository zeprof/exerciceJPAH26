package al420445.dao;

import al420445.airport.Airport;
import al420445.airport.Passenger;

import jakarta.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class AirportDaoImpl implements AirportDao {

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
        executeInTransaction(em -> {
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

    /**
     * Helper that reduces transaction boilerplate.
     * Wraps begin/commit/rollback/close in a single place.
     */
    private <T> T executeInTransaction(Function<EntityManager, T> action) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T result = action.apply(em);
            tx.commit();
            return result;
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }
}
