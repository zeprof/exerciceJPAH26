package al420445;

import al420445.airport.Passenger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main2 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernate2.ex1");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        final Passenger p2 = em.find(Passenger.class, Integer.valueOf(1));
        System.out.println(p2);
        p2.setName("Manolo");

        em.getTransaction().commit();

        em.close();
        emf.close();
    }
}
