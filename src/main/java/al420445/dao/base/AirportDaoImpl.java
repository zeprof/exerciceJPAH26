package al420445.dao.base;

import al420445.dao.tx.TransactionalDao;

import al420445.airport.Airport;
import al420445.airport.Passenger;

import jakarta.persistence.*;
import java.util.Collections;
import java.util.List;

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
        new TransactionalDao<Void>() {
            @Override
            protected Void executeInTransaction(EntityManager em) {
                Airport airport = em.find(Airport.class, (long) airportId);
                if (airport == null) return null;

                Passenger newPassenger = new Passenger(name);
                airport.addPassenger(newPassenger);
                em.persist(newPassenger);
                return null;
            }
        }.executeInTransaction();
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
