package al420445;

import al420445.airport.Passenger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.sql.SQLException;

/**
 * Ex02: Demonstrates JPA dirty checking.
 * Modifying a managed entity inside a transaction automatically generates an UPDATE.
 */
public class Ex02B_DirtyChecking {
    public static Long uneVariable = null;

    public static void main(String[] args) throws InterruptedException, SQLException {
        IO.println(getUneVariable());
        IO.println(getUneVariable());
        IO.println(getUneVariable());

        TcpServer.createTcpServer();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernate2.ex1");
        Ex01_PersistEntities.insertDataInDb(emf);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        final Passenger p2 = em.find(Passenger.class, 1L);
        IO.println(p2.getTickets());

        em.getTransaction().commit();
        em.close();
        emf.close();


        Thread.currentThread().join();
    }

    public static Long getUneVariable() {
        if(uneVariable == null)
            uneVariable = 1L;
        return uneVariable;
    }
}
