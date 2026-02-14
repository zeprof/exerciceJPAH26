package al420445;

import al420445.airport.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.sql.SQLException;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) throws InterruptedException, SQLException {
        TcpServer.createTcpServer();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernate2.ex1");

        insertDataInDb(emf);

        var em = emf.createEntityManager();
        em.getTransaction().begin();

        final Airport airport = em.find(Airport.class, 1);
        em.getTransaction().commit();

        System.out.println(airport.getPassengers().size());
        em.close();

        emf.close();

        Thread.currentThread().join();

    }

    public static Result insertDataInDb(EntityManagerFactory emf) {
        var em = emf.createEntityManager();
        em.getTransaction().begin();

        Airport airport = new Airport("Henri Coanda");

        Passenger passenger1 = new Passenger("Moukaila Smith");
        Address address = new Address();
        address.setStreet("street");
        address.setCity("city");
        address.setProvince("province");
        address.setCodePostal("code");
        passenger1.setAddress(address);
        passenger1.setAirport(airport);

        OneWayTicket ticket1 = new OneWayTicket();
        ticket1.setNumber("AA1234");
        ticket1.setLatestDepartureDate(LocalDate.now());
        ticket1.setPassenger(passenger1);

        ReturnTicket ticket2 = new ReturnTicket();
        ticket2.setNumber("BB5678");
        ticket2.setLatestReturnDate(LocalDate.now());
        ticket2.setPassenger(passenger1);

        passenger1.addTicket(ticket1);
        passenger1.addTicket(ticket2);

        Passenger passenger2 = new Passenger("Michael Johnson");
        passenger2.setAirport(airport);
        airport.addPassenger(passenger1);
        airport.addPassenger(passenger2);

        OneWayTicket ticket3 = new OneWayTicket();
        ticket3.setNumber("CC6767");
        ticket3.setLatestDepartureDate(LocalDate.now());
        ticket3.setPassenger(passenger2);
        passenger2.addTicket(ticket3);

        em.persist(airport);
        em.persist(passenger1);
        em.persist(passenger2);
        em.getTransaction().commit();
        em.close();

        Result result = new Result(airport, passenger1, passenger2);
        return result;
    }

//    public void insertOrdersInDb() {
//        Product p1 = Product.builder()
//                .productName("crayon")
//                .build();
//        Product p2 = Product.builder()
//                .productName("efface")
//                .build();
//        Product p3 = Product.builder()
//                .productName("regle")
//                .build();
//    }

    public record Result(Airport airport, Passenger passenger1, Passenger passenger2) {
    }
}
