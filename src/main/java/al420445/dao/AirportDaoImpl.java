package al420445.dao;

import al420445.airport.Airport;
import al420445.airport.Passenger;

import jakarta.persistence.*;
import java.util.Collections;
import java.util.List;

public class AirportDaoImpl implements AirportDao {
    public List<Airport> getAirports() {
        EntityManagerFactory emf = JpaUtil.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            List<Airport> airports = em.createQuery("select airport from Airport airport", Airport.class).getResultList();

            tx.commit();

            return airports;
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public void addPassenger(String name, int airportId) {
        EntityManagerFactory emf = JpaUtil.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            TypedQuery<Passenger> query = em.createQuery("select p from Passenger p where p.name = :name", Passenger.class);
            query.setParameter("name", name);
            List<Passenger> passengers = query.getResultList();

            if (!passengers.isEmpty()) {
                // If passenger exists, associate with airport if needed
                Passenger existing = passengers.get(0);
                Airport airport = em.find(Airport.class, airportId);
                if (airport != null) {
                    existing.setAirport(airport);
                    airport.addPassenger(existing);
                    em.merge(existing);
                    em.merge(airport);
                }
            } else {
                // create new passenger and associate
                Airport airport = em.find(Airport.class, airportId);
                Passenger newPassenger = new Passenger(name);
                if (airport != null) {
                    newPassenger.setAirport(airport);
                    airport.addPassenger(newPassenger);
                }
                em.persist(newPassenger);
                if (airport != null) em.merge(airport);
            }

            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Passenger> findPassengersByName(String passengersByName) {
        EntityManagerFactory emf = JpaUtil.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            TypedQuery<Passenger> queryPassenger = em.createQuery("select p from Passenger p where lower(p.name) like :name", Passenger.class);
            queryPassenger.setParameter("name", passengersByName.toLowerCase() + "%");
            List<Passenger> result = queryPassenger.getResultList();

            tx.commit();
            return result == null ? Collections.emptyList() : result;
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }
}
