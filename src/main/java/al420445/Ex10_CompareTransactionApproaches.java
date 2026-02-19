package al420445;

import al420445.dao.AirportDao;
import al420445.dao.AirportDaoFactory;
import al420445.dao.AirportDaoFactory.Strategy;
import al420445.service.AirportService;

/**
 * Demonstration comparing Template Method Pattern vs Functional Interface approach
 * for transaction management in the DAO layer.
 *
 * This class shows how to switch between the two implementations.
 */
public class Ex10_CompareTransactionApproaches {

    public static void main(String[] args) {
        Ex01_PersistEntities.insertDataInDb();

        System.out.println("=== Comparing Transaction Management Approaches ===\n");

        // Approach 1: Template Method Pattern
        System.out.println("1. Using TEMPLATE METHOD Pattern:");
        System.out.println("   - Uses TransactionalDao abstract class");
        System.out.println("   - Anonymous inner classes");
        System.out.println("   - No functional interfaces needed");
        demonstrateTemplateMethod();

        System.out.println("\n" + "=".repeat(60) + "\n");

        // Approach 2: Functional Interface
        System.out.println("2. Using FUNCTIONAL INTERFACE Approach:");
        System.out.println("   - Uses TransactionExecutor utility");
        System.out.println("   - Lambda expressions");
        System.out.println("   - Function<EntityManager, T> interface");
        demonstrateFunctional();

        System.out.println("\n" + "=".repeat(60) + "\n");

        // Approach 3: Using Factory to switch
        System.out.println("3. Using FACTORY to switch implementations:");
        demonstrateFactory();

        System.out.println("\n=== Both approaches produce the same result! ===");
    }

    private static void demonstrateTemplateMethod() {
        // Create service using Template Method implementation
        AirportService service = new AirportService(Strategy.TEMPLATE_METHOD);

        System.out.println("   Creating service with Template Method DAO...");
        System.out.println("   Airports: " + service.getAirports());
        System.out.println("   ✓ Template Method approach working!");
    }

    private static void demonstrateFunctional() {
        // Create service using Functional Interface implementation
        AirportService service = new AirportService(Strategy.FUNCTIONAL_INTERFACE);

        System.out.println("   Creating service with Functional DAO...");
        System.out.println("   Airports: " + service.getAirports());
        System.out.println("   ✓ Functional Interface approach working!");
    }

    private static void demonstrateFactory() {
        // Change default strategy globally
        System.out.println("   Setting default strategy to TEMPLATE_METHOD...");
        AirportDaoFactory.setDefaultStrategy(Strategy.TEMPLATE_METHOD);
        AirportService service1 = new AirportService();
        System.out.println("   ✓ Service created with: " + service1.getClass().getSimpleName());

        System.out.println("\n   Switching default strategy to FUNCTIONAL_INTERFACE...");
        AirportDaoFactory.setDefaultStrategy(Strategy.FUNCTIONAL_INTERFACE);
        AirportService service2 = new AirportService();
        System.out.println("   ✓ Service created with: " + service2.getClass().getSimpleName());

        // Or create specific implementation directly
        System.out.println("\n   Creating DAOs directly:");
        AirportDao daoTemplate = AirportDaoFactory.createTemplateMethod();
        System.out.println("   ✓ Created: " + daoTemplate.getClass().getSimpleName());

        AirportDao daoFunctional = AirportDaoFactory.createFunctional();
        System.out.println("   ✓ Created: " + daoFunctional.getClass().getSimpleName());
    }
}

