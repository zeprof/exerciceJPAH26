package al420445;

import al420445.airport.Airport;
import al420445.dao.base.JpaUtil;
import al420445.dao.examples.AirportDaoExamples;

/**
 * Ex11: Demonstrates the use of AirportDaoExamples, which showcases
 * the Template Method pattern (TransactionalDao) for managing JPA transactions.
 *
 * Each method in AirportDaoExamples creates an anonymous subclass of
 * TransactionalDao<T> that implements the transactional logic without
 * exposing the boilerplate transaction management code.
 */
public class Ex11_AirportDaoExamples {

    public static void main(String[] args) {
        AirportDaoExamples dao = new AirportDaoExamples();

        // ---- 1. Create airports ----------------------------------------
        System.out.println("=== 1. Creating airports ===");
        Airport yul = dao.createAirport("Montréal-Trudeau (YUL)");
        Airport yyz = dao.createAirport("Toronto Pearson (YYZ)");
        Airport cdg = dao.createAirport("Paris Charles de Gaulle (CDG)");
        System.out.printf("Created: %s (id=%d)%n", yul.getName(), yul.getId());
        System.out.printf("Created: %s (id=%d)%n", yyz.getName(), yyz.getId());
        System.out.printf("Created: %s (id=%d)%n", cdg.getName(), cdg.getId());

        // ---- 2. Check existence ----------------------------------------
        System.out.println("\n=== 2. Checking airport existence ===");
        System.out.printf("'%s' exists? %b%n", yul.getName(), dao.airportExists(yul.getName()));
        System.out.printf("'%s' exists? %b%n", "Unknown Airport", dao.airportExists("Unknown Airport"));

        // ---- 3. Count passengers (should be 0 at this point) -----------
        System.out.println("\n=== 3. Counting passengers (before any transfer) ===");
        long count = dao.countPassengers(yul.getId());
        System.out.printf("Passengers at '%s': %d%n", yul.getName(), count);

        // ---- 4. Rename an airport (dirty checking demo) ----------------
        System.out.println("\n=== 4. Renaming an airport ===");
        String newName = "Aéroport international Montréal-Trudeau";
        dao.updateAirportName(yul.getId(), newName);
        System.out.printf("Airport %d renamed to '%s'%n", yul.getId(), newName);
        System.out.printf("New name still exists? %b%n", dao.airportExists(newName));
        System.out.printf("Old name still exists? %b%n", dao.airportExists(yul.getName()));

        // ---- 5. Delete an airport --------------------------------------
        System.out.println("\n=== 5. Deleting an airport ===");
        System.out.printf("'%s' exists before delete? %b%n", cdg.getName(), dao.airportExists(cdg.getName()));
        dao.deleteAirport(cdg.getId());
        System.out.printf("'%s' exists after delete?  %b%n", cdg.getName(), dao.airportExists(cdg.getName()));

        // ---- Cleanup ---------------------------------------------------
        JpaUtil.close();
        System.out.println("\nDone.");
    }
}

