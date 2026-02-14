package al420445;

import al420445.airport.OneWayTicket;
import al420445.airport.Passenger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.time.LocalDate;

public class Main7 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernate2.ex1");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        final Passenger passenger = em.find(Passenger.class, 2);
        final OneWayTicket oneWayTicket = new OneWayTicket(LocalDate.now());
        oneWayTicket.setNumber("AA12345");
        passenger.addTicket(oneWayTicket);

        em.getTransaction().commit();
        em.close();
    }
}
