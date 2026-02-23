package al420445;

import al420445.airport.Airport;
import al420445.airport.OneWayTicket;
import al420445.airport.PassengerTicketCountDTO;

import al420445.airport.Passenger;
import al420445.airport.Ticket;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Ex06: Demonstrates various JPQL features.
 *
 * Covered topics:
 *  1. Implicit join (navigating a relationship)
 *  2. JOIN FETCH (avoiding N+1)
 *  3. DTO projection with 'new' keyword
 *  4. Named parameters
 *  5. Positional parameters
 *  6. Pagination (setFirstResult / setMaxResults)
 *  7. COUNT / aggregate functions
 *  8. ORDER BY
 *  9. BETWEEN with dates
 * 10. UPPER / LOWER string functions
 * 11. LIKE with wildcard
 * 12. IS NULL / IS NOT NULL
 * 13. IN clause
 * 14. DISTINCT
 * 15. Subquery with EXISTS
 * 16. Polymorphic query (querying a subclass)
 * 17. CONCAT / LENGTH string functions
 * 18. Embedded attribute query (querying inside @Embedded)
 * 19. LEFT JOIN FETCH
 * 20. UPDATE query (bulk update)
 * 21. DELETE query (bulk delete)
 * 22. CASE expression
 * 23. COALESCE / NULLIF
 * 24. SIZE function on a collection
 */
public class Ex06_JoinFetchAndDTO {
    public static void main(String[] args) throws InterruptedException, SQLException {
        TcpServer.createTcpServer();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernate2.ex1");
        Ex01_PersistEntities.insertDataInDb(emf);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        // ──────────────────────────────────────────────
        // 1. Implicit join – navigating a relationship
        // ──────────────────────────────────────────────
        System.out.println("=== 1. Implicit join ===");
        final TypedQuery<Passenger> pass =
                em.createQuery(
            "select p from Passenger p where lower(p.airport.name) like 'henri%'", Passenger.class);
        final List<Passenger> passengers = pass.getResultList();
        System.out.println(passengers);

        // ──────────────────────────────────────────────
        // 2. JOIN FETCH – loads passengers in one query
        // ──────────────────────────────────────────────
        System.out.println("\n=== 2. JOIN FETCH ===");
        final TypedQuery<Airport> airportQuery =
                em.createQuery(
            "select a from Airport a join fetch a.passengers where lower(a.name) like 'henri%'", Airport.class);
        final Airport airport = airportQuery.getSingleResult();
        System.out.println(airport);

        // ──────────────────────────────────────────────
        // 3. DTO projection with 'new'
        // ──────────────────────────────────────────────
        System.out.println("\n=== 3. DTO projection ===");
        var queryStr = """
                select new al420445.airport.PassengerTicketCountDTO(count(t), t.passenger) 
                from Ticket t 
                group by t.passenger
        """;
        final List<PassengerTicketCountDTO> ticketCounts = em.createQuery(
                queryStr, PassengerTicketCountDTO.class).getResultList();
        ticketCounts.forEach(dto ->
            System.out.println(dto.count() + " ticket(s) for " + dto.passenger())
        );

        // ──────────────────────────────────────────────
        // 4. Named parameters (:name)
        // ──────────────────────────────────────────────
        System.out.println("\n=== 4. Named parameters ===");
        List<Passenger> byName = em.createQuery(
                "select p from Passenger p where p.name = :passengerName", Passenger.class)
                .setParameter("passengerName", "Moukaila Smith")
                .getResultList();
        byName.forEach(System.out::println);

        // ──────────────────────────────────────────────
        // 5. Positional parameters (?1)
        // ──────────────────────────────────────────────
        System.out.println("\n=== 5. Positional parameters ===");
        List<Passenger> byNamePositional = em.createQuery(
                "select p from Passenger p where p.name = ?1", Passenger.class)
                .setParameter(1, "Michael Johnson")
                .getResultList();
        byNamePositional.forEach(System.out::println);

        // ──────────────────────────────────────────────
        // 6. Pagination (setFirstResult / setMaxResults)
        // ──────────────────────────────────────────────
        System.out.println("\n=== 6. Pagination ===");
        List<Passenger> page = em.createQuery(
                "select p from Passenger p order by p.name", Passenger.class)
                .setFirstResult(0)  // offset (skip 0)
                .setMaxResults(1)   // limit to 1 result
                .getResultList();
        System.out.println("Page 1 (size=1): " + page);

        // ──────────────────────────────────────────────
        // 7. COUNT aggregate function
        // ──────────────────────────────────────────────
        System.out.println("\n=== 7. COUNT ===");
        Long passengerCount = em.createQuery(
                "select count(p) from Passenger p", Long.class)
                .getSingleResult();
        System.out.println("Total passengers: " + passengerCount);

        // ──────────────────────────────────────────────
        // 8. ORDER BY
        // ──────────────────────────────────────────────
        System.out.println("\n=== 8. ORDER BY ===");
        List<Passenger> ordered = em.createQuery(
                "select p from Passenger p order by p.name desc", Passenger.class)
                .getResultList();
        ordered.forEach(System.out::println);

        // ──────────────────────────────────────────────
        // 9. BETWEEN with dates
        // ──────────────────────────────────────────────
        System.out.println("\n=== 9. BETWEEN (dates) ===");
        List<OneWayTicket> ticketsInRange = em.createQuery(
                "select t from OneWayTicket t where t.latestDepartureDate between :start and :end",
                OneWayTicket.class)
                .setParameter("start", LocalDate.now().minusDays(1))
                .setParameter("end", LocalDate.now().plusDays(1))
                .getResultList();
        ticketsInRange.forEach(t -> System.out.println("Ticket " + t.getNumber() + " departs " + t.getLatestDepartureDate()));

        // ──────────────────────────────────────────────
        // 10. UPPER / LOWER string functions
        // ──────────────────────────────────────────────
        System.out.println("\n=== 10. UPPER / LOWER ===");
        List<String> upperNames = em.createQuery(
                "select upper(p.name) from Passenger p", String.class)
                .getResultList();
        upperNames.forEach(System.out::println);

        // ──────────────────────────────────────────────
        // 11. LIKE with wildcard
        // ──────────────────────────────────────────────
        System.out.println("\n=== 11. LIKE ===");
        List<Passenger> likeResult = em.createQuery(
                "select p from Passenger p where p.name like :pattern", Passenger.class)
                .setParameter("pattern", "%Smith")
                .getResultList();
        likeResult.forEach(System.out::println);

        // ──────────────────────────────────────────────
        // 12. IS NULL / IS NOT NULL
        // ──────────────────────────────────────────────
        System.out.println("\n=== 12. IS NULL / IS NOT NULL ===");
        List<Passenger> withAddress = em.createQuery(
                "select p from Passenger p where p.address.city is not null", Passenger.class)
                .getResultList();
        System.out.println("Passengers with an address: " + withAddress);

        List<Passenger> withoutAddress = em.createQuery(
                "select p from Passenger p where p.address.city is null", Passenger.class)
                .getResultList();
        System.out.println("Passengers without an address: " + withoutAddress);

        // ──────────────────────────────────────────────
        // 13. IN clause
        // ──────────────────────────────────────────────
        System.out.println("\n=== 13. IN clause ===");
        List<Passenger> inResult = em.createQuery(
                "select p from Passenger p where p.name in :names", Passenger.class)
                .setParameter("names", List.of("Moukaila Smith", "Michael Johnson"))
                .getResultList();
        inResult.forEach(System.out::println);

        // ──────────────────────────────────────────────
        // 14. DISTINCT
        // ──────────────────────────────────────────────
        System.out.println("\n=== 14. DISTINCT ===");
        List<String> distinctCities = em.createQuery(
                "select distinct p.address.city from Passenger p where p.address.city is not null",
                String.class)
                .getResultList();
        System.out.println("Distinct cities: " + distinctCities);

        // ──────────────────────────────────────────────
        // 15. Subquery with EXISTS
        // ──────────────────────────────────────────────
        System.out.println("\n=== 15. EXISTS subquery ===");
        List<Passenger> withTickets = em.createQuery(
                "select p from Passenger p where exists (select t from Ticket t where t.passenger = p)",
                Passenger.class)
                .getResultList();
        System.out.println("Passengers who have at least one ticket: " + withTickets);

        // ──────────────────────────────────────────────
        // 16. Polymorphic query (querying a subclass)
        // ──────────────────────────────────────────────
        System.out.println("\n=== 16. Polymorphic query ===");
        // Querying the abstract Ticket returns both OneWay and Return tickets
        List<Ticket> allTickets = em.createQuery(
                "select t from Ticket t", Ticket.class)
                .getResultList();
        System.out.println("All tickets (polymorphic): " + allTickets.size());

        // Querying only OneWayTicket
        List<OneWayTicket> oneWayOnly = em.createQuery(
                "select t from OneWayTicket t", OneWayTicket.class)
                .getResultList();
        System.out.println("OneWayTickets only: " + oneWayOnly.size());

        // TYPE() discriminator function
        List<Ticket> returnOnly = em.createQuery(
                "select t from Ticket t where type(t) = ReturnTicket", Ticket.class)
                .getResultList();
        System.out.println("ReturnTickets via TYPE(): " + returnOnly.size());

        // ──────────────────────────────────────────────
        // 17. CONCAT / LENGTH string functions
        // ──────────────────────────────────────────────
        System.out.println("\n=== 17. CONCAT / LENGTH ===");
        List<String> labels = em.createQuery(
                "select concat(p.name, ' (id=', cast(p.id as string), ')') from Passenger p",
                String.class)
                .getResultList();
        labels.forEach(System.out::println);

        Integer nameLength = em.createQuery(
                "select length(p.name) from Passenger p where p.name = 'Moukaila Smith'",
                Integer.class)
                .getSingleResult();
        System.out.println("Length of 'Moukaila Smith': " + nameLength);

        // ──────────────────────────────────────────────
        // 18. Embedded attribute query
        // ──────────────────────────────────────────────
        System.out.println("\n=== 18. Embedded attribute query ===");
        List<Passenger> byCity = em.createQuery(
                "select p from Passenger p where p.address.city = :city", Passenger.class)
                .setParameter("city", "city")
                .getResultList();
        byCity.forEach(p -> System.out.println(p.getName() + " lives in " + p.getAddress().getCity()));

        // ──────────────────────────────────────────────
        // 19. LEFT JOIN FETCH
        // ──────────────────────────────────────────────
        System.out.println("\n=== 19. LEFT JOIN FETCH ===");
        // LEFT JOIN FETCH also loads passengers with no tickets
        List<Passenger> allWithTickets = em.createQuery(
                "select distinct p from Passenger p left join fetch p.tickets", Passenger.class)
                .getResultList();
        allWithTickets.forEach(p ->
            System.out.println(p.getName() + " has " + p.getTickets().size() + " ticket(s)")
        );

        // ──────────────────────────────────────────────
        // 20. CASE expression
        // ──────────────────────────────────────────────
        System.out.println("\n=== 20. CASE expression ===");
        List<String> ticketTypes = em.createQuery("""
                select case type(t)
                    when OneWayTicket then concat('OneWay: ', t.number)
                    when ReturnTicket then concat('Return: ', t.number)
                    else 'Unknown'
                end
                from Ticket t
                """, String.class)
                .getResultList();
        ticketTypes.forEach(System.out::println);

        // ──────────────────────────────────────────────
        // 21. COALESCE / NULLIF
        // ──────────────────────────────────────────────
        System.out.println("\n=== 21. COALESCE / NULLIF ===");
        // COALESCE returns the first non-null value
        List<String> coalesced = em.createQuery(
                "select coalesce(p.address.city, 'N/A') from Passenger p", String.class)
                .getResultList();
        System.out.println("Cities (coalesced): " + coalesced);

        // NULLIF returns null if the two arguments are equal
        List<String> nullified = em.createQuery(
                "select nullif(p.address.city, 'city') from Passenger p where p.address.city is not null",
                String.class)
                .getResultList();
        System.out.println("Cities (nullif 'city'): " + nullified);

        // ──────────────────────────────────────────────
        // 22. SIZE function on a collection
        // ──────────────────────────────────────────────
        System.out.println("\n=== 22. SIZE() ===");
        List<Passenger> withMultipleTickets = em.createQuery(
                "select p from Passenger p where size(p.tickets) > 1", Passenger.class)
                .getResultList();
        System.out.println("Passengers with >1 ticket: " + withMultipleTickets);

        // ──────────────────────────────────────────────
        // 23. Multi-select / Object[] projection
        // ──────────────────────────────────────────────
        System.out.println("\n=== 23. Object[] projection ===");
        List<Object[]> tuples = em.createQuery(
                "select p.name, size(p.tickets) from Passenger p group by p.name, p.id",
                Object[].class)
                .getResultList();
        tuples.forEach(row -> System.out.println(row[0] + " → " + row[1] + " ticket(s)"));

        em.getTransaction().commit();

        // ──────────────────────────────────────────────
        // 24. Bulk UPDATE (requires its own transaction)
        // ──────────────────────────────────────────────
        System.out.println("\n=== 24. Bulk UPDATE ===");
        em = emf.createEntityManager();
        em.getTransaction().begin();
        int updated = em.createQuery(
                "update Airport a set a.name = concat(a.name, ' (International)') where a.name not like '%(International)'")
                .executeUpdate();
        System.out.println("Airports updated: " + updated);
        em.getTransaction().commit();
        em.close();

        // ──────────────────────────────────────────────
        // 25. Bulk DELETE (requires its own transaction)
        // ──────────────────────────────────────────────
        // NOTE: Commented out to keep data intact for inspection
        // System.out.println("\n=== 25. Bulk DELETE ===");
        // em = emf.createEntityManager();
        // em.getTransaction().begin();
        // int deleted = em.createQuery(
        //         "delete from OneWayTicket t where t.latestDepartureDate < :cutoff")
        //         .setParameter("cutoff", LocalDate.now().minusDays(30))
        //         .executeUpdate();
        // System.out.println("Old one-way tickets deleted: " + deleted);
        // em.getTransaction().commit();
        // em.close();

        emf.close();

        // Keep JVM alive so the H2 TCP server remains accessible
        Thread.currentThread().join();
    }
}
